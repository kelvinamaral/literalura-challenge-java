package com.alura.literalura.principal;

import com.alura.literalura.dto.AutorDTO;
import com.alura.literalura.dto.LivroDTO;
import com.alura.literalura.dto.RespostaLivroDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Livro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvertDados;
import com.alura.literalura.service.LivroService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Autowired
    private LivroService livroService;

    @Autowired
    private AutorService autorService;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvertDados convertDados;

    private static final String BASE_URL = "https://gutendex.com/books/";

    public void exibirMenu() {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n*** LITERALURA ***");
            System.out.println("1 - Buscar livro pelo título");
            System.out.println("2 - Listar todos os livros cadastrados");
            System.out.println("3 - Listar todos os autores cadastrados");
            System.out.println("4 - Listar autores vivos no ano informado");
            System.out.println("5 - Listar livros por idioma");
            System.out.println("6 - Listar os 10 livros mais baixados");
            System.out.println("7 - Exibir estatísticas do banco de dados");
            System.out.println("0 - Sair do sistema");
            System.out.print("Escolha uma opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); // Limpa buffer

            switch (opcao) {
                case 1:
                    System.out.print("Digite o título do livro: ");
                    String titulo = sc.nextLine();
                    try {
                        String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
                        String json = consumoAPI.obterDados(BASE_URL + "?search=" + encodedTitulo);
                        RespostaLivroDTO respostaLivroDTO = convertDados.obterDados(json, RespostaLivroDTO.class);
                        List<LivroDTO> livrosDTO = respostaLivroDTO.getLivros();

                        if (livrosDTO.isEmpty()) {
                            System.out.println("Livro não encontrado na API.");
                        } else {
                            boolean livroRegistrado = false;
                            for (LivroDTO livroDTO : livrosDTO) {
                                if (livroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                                    Optional<Livro> livroExistente = livroService.obterLivroPorTitulo(titulo);
                                    if (livroExistente.isPresent()) {
                                        System.out.println("Aviso: O livro com o título \"" + titulo + "\" já está cadastrado.");
                                        livroRegistrado = true;
                                        break;
                                    } else {
                                        Livro livro = new Livro();
                                        livro.setTitulo(livroDTO.getTitulo());
                                        livro.setIdioma(livroDTO.getIdiomas().get(0));
                                        livro.setNumeroDownloads(livroDTO.getNumeroDownloads());

                                        // Cria um novo autor
                                        AutorDTO primerAutorDTO = livroDTO.getAutores().get(0);
                                        Autor autor = autorService.obterAutorPorNome(primerAutorDTO.getNome())
                                                .orElseGet(() -> {
                                                    Autor novoAutor = new Autor();
                                                    novoAutor.setNome(primerAutorDTO.getNome());

                                                    //
                                                    novoAutor.setAnoNascimento(primerAutorDTO.getAnoNascimento());
                                                    novoAutor.setAnoFalecimento(primerAutorDTO.getAnoFalecimento());

                                                    return autorService.criarAutor(novoAutor);
                                                });

                                        livro.setAutor(autor);
                                        livroService.criarLivro(livro);
                                        System.out.println("Livro registrado com sucesso: " + livro.getTitulo());
                                        exibirDetalhesLivro(livroDTO);
                                        livroRegistrado = true;
                                        break;
                                    }
                                }
                            }
                            if (!livroRegistrado) {
                                System.out.println("Não foi encontrado um livro com o título exato \"" + titulo + "\".");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao obter dados da API: " + e.getMessage());
                    }
                    break;

                case 2:
                    livroService.listarLivros().forEach(livro -> {
                        System.out.println("------ LIVRO ------");
                        System.out.println("Título: " + livro.getTitulo());
                        System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                        System.out.println("Idioma: " + livro.getIdioma());
                        System.out.println("Número de downloads: " + livro.getNumeroDownloads());
                    });
                    break;

                case 3:
                    autorService.listarAutores().forEach(autor -> {
                        System.out.println("------ AUTOR ------");
                        System.out.println("Nome: " + autor.getNome());
                        System.out.println("Ano de nascimento: " + autor.getAnoNascimento());
                        System.out.println("Ano de falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Desconhecido"));
                        String livros = autor.getLivros().stream()
                                .map(Livro::getTitulo)
                                .collect(Collectors.joining(", "));
                        System.out.println("Livros: [ " + livros + " ]");
                    });
                    break;

                case 4:
                    System.out.print("Digite o ano para buscar autores vivos: ");
                    int ano = sc.nextInt();
                    sc.nextLine();
                    List<Autor> autoresVivos = autorService.listarAutoresVivosPorAno(ano);
                    if (autoresVivos.isEmpty()) {
                        System.out.println("Nenhum autor encontrado vivo no ano " + ano);
                    } else {
                        autoresVivos.forEach(autor -> {
                            System.out.println("------ AUTOR VIVO ------");
                            System.out.println("Nome: " + autor.getNome());
                            System.out.println("Ano de nascimento: " + autor.getAnoNascimento());
                            System.out.println("Ano de falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Ainda vivo"));
                            System.out.println("Número de livros: " + autor.getLivros().size());
                        });
                    }
                    break;

                case 5:
                    System.out.println("Digite o idioma (ex: pt, en, es, fr): ");
                    String idioma = sc.nextLine();
                    if (List.of("pt", "en", "es", "fr").contains(idioma.toLowerCase())) {
                        livroService.listarLivrosPorIdioma(idioma).forEach(livro -> {
                            System.out.println("------ LIVRO ------");
                            System.out.println("Título: " + livro.getTitulo());
                            System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                            System.out.println("Idioma: " + livro.getIdioma());
                            System.out.println("Número de downloads: " + livro.getNumeroDownloads());
                        });
                    } else {
                        System.out.println("Idioma inválido. Tente novamente.");
                    }
                    break;

                case 0:
                    System.out.println("Encerrando o sistema. Até logo!");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);

        sc.close();
    }

    /**
     * Exibe os detalhes de um LivroDTO no console.
     *
     * @param livroDTO Objeto contendo os dados do livro.
     */
//    private void exibirDetalhesLivro(LivroDTO livroDTO) {
//        System.out.println("------ LIVRO ------");
//        System.out.println("Título: " + livroDTO.getTitulo());
//        System.out.println("Idioma: " + String.join(", ", livroDTO.getIdiomas()));
//        System.out.println("Downloads: " + livroDTO.getNumeroDownloads());
//
//        livroDTO.getAutores().forEach(autor -> {
//            System.out.println("Autor: " + autor.getNome());
//            System.out.println("Nascimento: " + autor.getAnoNascimento());
//            System.out.println("Falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Ainda vivo"));
//        });
//    }

    private void exibirDetalhesLivro(LivroDTO livroDTO) {
        System.out.println("------LIVRO--------");
        System.out.println("Título: " + livroDTO.getTitulo());
        System.out.println("Autor: " + (livroDTO.getAutores().isEmpty() ? "Desconocido" : livroDTO.getAutores().get(0).getNome()));
        System.out.println("Idioma: " + livroDTO.getIdiomas().get(0));
        System.out.println("Número de descargas: " + livroDTO.getNumeroDownloads());
    }
}
