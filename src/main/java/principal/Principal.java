package principal;

import java.util.Scanner;

public class Principal {

    private Scanner sc = new Scanner(System.in);

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
        var opcao = sc.nextLine();

    }
}
