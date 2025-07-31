package com.alura.literalura.principal;

import com.alura.literalura.dto.AutorDTO;
import com.alura.literalura.dto.LivroDTO;
import com.alura.literalura.dto.RespostaLivroDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Livro;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvertDados;
import com.alura.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    /**
     * Serviço para gerenciar livros.
     * Injetado automaticamente pelo Spring.
     */
    @Autowired
    private LivroService livroService;

    /**
     * Serviço para gerenciar autores.
     * Injetado automaticamente pelo Spring.
     */
    @Autowired
    private AutorService autorService;

    /**
     * Serviço para consumir dados de uma API externa.
     * Injetado automaticamente pelo Spring.
     */
    @Autowired
    private ConsumoAPI consumoAPI;

    /**
     * Serviço para converter dados JSON para objetos DTO.
     * Injetado automaticamente pelo Spring.
     */
    @Autowired
    private ConvertDados converteDados;

    /**
     * URL base da API externa de livros.
     */
    private static final String BASE_URL = "https://gutendx.com/books/";

    /**
     * Exibe o menu principal e gerencia as opções selecionadas pelo usuário.
     */
    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("--- LITERALURA ---");
            System.out.println("1 - Buscar livro por título");
            System.out.println("2 - Listar livros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos em um ano");
            System.out.println("5 - Listar livros por idioma");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    System.out.print("Digite o título do livro: ");
                    String titulo = scanner.nextLine();
                    try {
                        String tituloCodeificado = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
                        String json = consumoAPI.obterDados(BASE_URL + "?search=" + tituloCodeificado);
                        RespostaLivroDTO respostaLivroDTO = converteDados.obterDados(json, RespostaLivroDTO.class);
                        List<LivroDTO> livrosDTO = respostaLivroDTO.getLivros();

                        if (livrosDTO.isEmpty()) {
                            System.out.println("Livro não encontrado na API");
                        } else {
                            boolean livroRegistrado = false;
                            for (LivroDTO livroDTO : livrosDTO) {
                                if (livroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                                    Optional<Livro> livroExistente = livroService.obterLivroPorTitulo(titulo);
                                    if (livroExistente.isPresent()) {
                                        System.out.println("Detalhe: Chave (titulo)=(" + titulo + ") já existe");
                                        System.out.println("Não é possível registrar o mesmo livro mais de uma vez");
                                        livroRegistrado = true;
                                        break;
                                    } else {
                                        Livro livro = new Livro();
                                        livro.setTitulo(livroDTO.getTitulo());
                                        livro.setIdioma(livroDTO.getIdiomas().get(0));
                                        livro.setNumeroDownloads(livroDTO.getNumeroDownloads());

                                        // Buscar ou criar o Autor
                                        AutorDTO primeiroAutorDTO = livroDTO.getAutores().get(0);
                                        Autor autor = autorService.obterAutorPorNome(primeiroAutorDTO.getNome())
                                                .orElseGet(() -> {
                                                    Autor novoAutor = new Autor();
                                                    novoAutor.setNome(primeiroAutorDTO.getNome());
                                                    novoAutor.setAnoNascimento(primeiroAutorDTO.getAnoNascimento());
                                                    novoAutor.setAnoFalecimento(primeiroAutorDTO.getAnoFalecimento());
                                                    return autorService.criarAutor(novoAutor);
                                                });

                                        // Associar o Autor ao Livro
                                        livro.setAutor(autor);

                                        // Salvar o livro no banco de dados
                                        livroService.criarLivro(livro);
                                        System.out.println("Livro registrado: " + livro.getTitulo());
                                        exibirDetalhesLivro(livroDTO);
                                        livroRegistrado = true;
                                        break;
                                    }
                                }
                            }
                            if (!livroRegistrado) {
                                System.out.println("Não foi encontrado um livro exatamente com o título '" + titulo + "' na API");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao obter dados da API: " + e.getMessage());
                    }
                    break;

                case 2:
                    livroService.listarLivros().forEach(livro -> {
                        System.out.println("------LIVRO--------");
                        System.out.println("Título: " + livro.getTitulo());
                        System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                        System.out.println("Idioma: " + livro.getIdioma());
                        System.out.println("Número de downloads: " + livro.getNumeroDownloads());
                    });
                    break;

                case 3:
                    autorService.listarAutores().forEach(autor -> {
                        System.out.println("-------AUTOR-------");
                        System.out.println("Autor: " + autor.getNome());
                        System.out.println("Data de nascimento: " + autor.getAnoNascimento());
                        System.out.println("Data de falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Desconhecido"));
                        String livros = autor.getLivros().stream()
                                .map(Livro::getTitulo)
                                .collect(Collectors.joining(", "));
                        System.out.println("Livros: [ " + livros + " ]");
                    });
                    break;

                case 4:
                    System.out.print("Digite o ano vivo de autor(es) que deseja buscar: ");
                    int ano = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha
                    List<Autor> autoresVivos = autorService.listarAutoresVivosPorAno(ano);
                    if (autoresVivos.isEmpty()) {
                        System.out.println("Não foram encontrados autores vivos no ano " + ano);
                    } else {
                        autoresVivos.forEach(autor -> {
                            System.out.println("-------AUTOR-------");
                            System.out.println("Autor: " + autor.getNome());
                            System.out.println("Data de nascimento: " + autor.getAnoNascimento());
                            System.out.println("Data de falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Desconhecido"));
                            System.out.println("Livros: " + autor.getLivros().size());
                        });
                    }
                    break;

                case 5:
                    System.out.println("Digite o idioma:");
                    System.out.println("pt");
                    System.out.println("en");
                    System.out.println("es");
                    System.out.println("fr");
                    String idioma = scanner.nextLine();
                    if ("pt".equalsIgnoreCase(idioma) || "en".equalsIgnoreCase(idioma) ||
                            "es".equalsIgnoreCase(idioma) || "fr".equalsIgnoreCase(idioma)) {
                        livroService.listarLivrosPorIdioma(idioma).forEach(livro -> {
                            System.out.println("------LIVRO--------");
                            System.out.println("Título: " + livro.getTitulo());
                            System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                            System.out.println("Idioma: " + livro.getIdioma());
                            System.out.println("Número de downloads: " + livro.getNumeroDownloads());
                        });
                    } else {
                        System.out.println("Idioma não válido. Tente novamente.");
                    }
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção não válida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    /**
     * Exibe os detalhes de um livro DTO.
     *
     * @param livroDTO O objeto LivroDTO cujos detalhes serão exibidos.
     */
    private void exibirDetalhesLivro(LivroDTO livroDTO) {
        System.out.println("------LIVRO--------");
        System.out.println("Título: " + livroDTO.getTitulo());
        System.out.println("Autor: " + (livroDTO.getAutores().isEmpty() ? "Desconhecido" : livroDTO.getAutores().get(0).getNome()));
        System.out.println("Idioma: " + livroDTO.getIdiomas().get(0));
        System.out.println("Número de downloads: " + livroDTO.getNumeroDownloads());
    }
}