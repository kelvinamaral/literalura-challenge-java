package principal;

import java.util.Scanner;

public class Principal {

    private Scanner sc = new Scanner(System.in);

    private final String URL_BASE = "https://gutendex.com/books/";

    public void exibeMenu(){

        var menu = """
                
                *** LITERALURA ***
                
                1 - Buscar livro pelo título  
                2 - Listar todos os livros cadastrados  
                3 - Listar todos os autores cadastrados  
                4 - Listar autores vivos no ano informado  
                5 - Listar livros por idioma  
                6 - Listar os 10 livros mais baixados  
                7 - Exibir estatísticas do banco de dados  
                
                0 - Sair do sistema
                """;

        System.out.println(menu);

        String endereco;

        if (sc.hasNextInt()) {
            int opcao = sc.nextInt();
            // colocar lógica baseada na opção
            System.out.println("Você escolheu a opção: " + opcao);
        } else {
            System.out.println("Erro! Digite um número inteiro.");
            sc.next();
        }

    }
}
