package principal;

import dto.AutorDTO;
import dto.LivroDTO;
import dto.RespostaLivroDTO;
import model.Autor;
import model.Livro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.AutorService;
import service.ConsumoAPI;
import service.ConvertDados;
import service.LivroService;

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

    public void exibirMenu(){
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
            sc.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Ingrese el título del libro: ");
                    String titulo = sc.nextLine();
                    try {
                        String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
                        String json = consumoAPI.obterDados(BASE_URL + "?search=" + encodedTitulo);
                        RespostaLivroDTO respostaLivroDTO = convertDados.obterDados(json, RespostaLivroDTO.class);
                        List<LivroDTO> livrosDTO = respostaLivroDTO.getLivros();
                        if (livrosDTO.isEmpty()) {
                            System.out.println("Libro no encontrado en la API");
                        } else {
                            boolean libroRegistrado = false;
                            for (LivroDTO livroDTO : livrosDTO) {
                                if (livroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                                    Optional<Livro> libroExistente = livroService.obterLivroPorTitulo(titulo);
                                    if (libroExistente.isPresent()) {
                                        System.out.println("Detalle: Clave (titulo)=(" + titulo + ") ya existe");
                                        System.out.println("No se puede registrar el mismo libro más de una vez");
                                        libroRegistrado = true;
                                        break;
                                    } else {
                                        Livro livro = new Livro();
                                        livro.setTitulo(livroDTO.getTitulo());
                                        livro.setIdioma(livroDTO.getIdiomas().get(0));
                                        livro.setNumeroDownloads(livroDTO.getNumeroDownloads());

                                        AutorDTO buscarAutor = livro.getAutor().get(0);
                                        Autor autor = autorService.obterAutorPorNome(buscarAutor.getNome())
                                                .orElseGet(() -> {
                                                    Autor novoAutor = new Autor();
                                                    novoAutor.setNome(buscarAutor.getNome());
                                                    novoAutor.setAnoNascimento(buscarAutor.getAnoNascimento());
                                                    novoAutor.setAnoFalecimento(buscarAutor.getAnoFalescimento());
                                                    return autorService.criarAutor(novoAutor);
                                                });

                                        // Asociar el Autor al Libro
                                        livro.setAutor(autor);

                                        // Guardar el libro en la base de datos
                                        livroService.criarLivro(livro);
                                        System.out.println("Libro registrado: " + livro.getTitulo());
                                        exibirDetalhesLivro(livroDTO);
                                        libroRegistrado = true;
                                        break;
                                    }
                                }
                            }
                            if (!livroRegistrado) {
                                System.out.println("No se encontró un libro exactamente con el título '" + titulo + "' en la API");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error al obtener datos de la API: " + e.getMessage());
                    }
                    break;
                case 2:
                    livroService.listarLivros().forEach(libro -> {
                        System.out.println("------LIVRO--------");
                        System.out.println("Título: " + libro.getTitulo());
                        System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNome() : "Desconocido"));
                        System.out.println("Idioma: " + libro.getIdioma());
                        System.out.println("Número de descargas: " + libro.getNumeroDownloads());
                    });
                    break;
                case 3:
                    autorService.listarAutores().forEach(autor -> {
                        System.out.println("-------AUTOR-------");
                        System.out.println("Autor: " + autor.getNome());
                        System.out.println("Fecha de nacimiento: " + autor.getAnoNascimento());
                        System.out.println("Fecha de fallecimiento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Desconhecido"));
                        String libros = autor.getLivros().stream()
                                .map(libros::getTitulo)
                                .collect(Collectors.joining(", "));
                        System.out.println("Libros: [ " + libros + " ]");
                    });
                    break;
                case 4:
                    System.out.print("Ingrese el año vivo de autor(es) que desea buscar: ");
                    int ano = sc.nextInt();
                    sc.nextLine(); // Consumir el salto de línea
                    List<Autor> autoresVivos = autorService.listarAutoresVivosPorAno(ano);
                    if (autoresVivos.isEmpty()) {
                        System.out.println("No se encontraron autores vivos en el año " + ano);
                    } else {
                        autoresVivos.forEach(autor -> {
                            System.out.println("-------AUTOR-------");
                            System.out.println("Autor: " + autor.getNome());
                            System.out.println("Fecha de nacimiento: " + autor.getAnoNascimento());
                            System.out.println("Fecha de fallecimiento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Desconocido"));
                            System.out.println("Libros: " + autor.getLivros().size());
                        });
                    }
                    break;
                case 5:
                    System.out.println("Ingrese el idioma:");
                    System.out.println("es");
                    System.out.println("en");
                    System.out.println("fr");
                    System.out.println("pt");
                    String idioma = sc.nextLine();
                    if ("es".equalsIgnoreCase(idioma) || "en".equalsIgnoreCase(idioma) || "fr".equalsIgnoreCase(idioma) || "pt".equalsIgnoreCase(idioma)) {
                        livroService.listarLivrosPorIdioma(idioma).forEach(livro -> {
                            System.out.println("------LIBRO--------");
                            System.out.println("Título: " + livro.getTitulo());
                            System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconocido"));
                            System.out.println("Idioma: " + livro.getIdioma());
                            System.out.println("Número de descargas: " + livro.getNumeroDownloads());
                        });
                    } else {
                        System.out.println("Idioma invalido. Tente de novo.");
                    }
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção invalida. Tente de novo.");
            }
        } while (opcao != 0);

        sc.close();
    }

    /**
     * Muestra los detalles de un libro DTO.
     *
     * @param livroDTO El objeto LibroDTO cuyos detalles se van a mostrar.
     */
    private void mostrarDetallesLibro(LivroDTO livroDTO) {
        System.out.println("------LIBRO--------");
        System.out.println("Título: " + livroDTO.getTitulo());
        System.out.println("Autor: " + (livroDTO.getAutores().isEmpty() ? "Desconhecido" : livroDTO.getAutores().get(0).getNome()));
        System.out.println("Idioma: " + livroDTO.getIdiomas().get(0));
        System.out.println("Número de descargas: " + livroDTO.getNumeroDownloads());
    }
}
