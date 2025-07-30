package principal;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal {

    private Scanner sc = new Scanner(System.in);

    public void exibeMenu() {

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

        int opcao = -1;
        while (opcao != 0) {
            System.out.println(menu);
            if (sc.hasNextInt()) {
                opcao = sc.nextInt();
                if (opcao < 0 || opcao > 7) {
                    System.out.println("Erro! Opção inválida. Tente novamente.");
                    continue;
                }
                switch (opcao) {
                    case 1:
                        System.out.println("Você escolheu a opção: 1");
                        break;
                    case 2:
                        System.out.println("Você escolheu a opção: 2");
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção ainda não implementada, tente novamente!");
                }
            } else {
                System.out.println("Erro! Digite um número inteiro.");
                sc.next();
            }
        }
    }
}
