package model;

import jakarta.persistence.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "ano_nascimento")
    private Year anoNascimento;

    @Column(name = "ano_falecimento")
    private Year anoFalecimento;

    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Livro> livros = new ArrayList<>();

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Year getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Year anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public Year getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setAnoFalecimento(Year anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    // Construtores
    public Autor() {}

    public static boolean possuiAno(Year ano) {
        return ano != null && !ano.equals(Year.of(0));
    }

    public Autor(AutorDTO autorDTO) {
        this.nome = autorDTO.autor();
        this.anoNascimento = autorDTO.anoNascimento() != null ? Year.of(autorDTO.anoNascimento()) : null;
        this.anoFalecimento = autorDTO.anoFalecimento() != null ? Year.of(autorDTO.anoFalecimento()) : null;
    }


    public Autor(String autor, Year anoNascimento, Year anoFalecimento) {
        this.nome = autor;
        this.anoNascimento = anoNascimento;
        this.anoFalecimento = anoFalecimento;
    }

    @Override
    public String toString() {
        String anoNascimentoStr = anoNascimento != null ? anoNascimento.toString() : "Desconhecido";
        String anoFalecimentoStr = anoFalecimento != null ? anoFalecimento.toString() : "Desconhecido";

        return "Autor: " + nome + " (nascido em " + anoNascimentoStr + ", falecido em " + anoFalecimentoStr + ")";
    }
}