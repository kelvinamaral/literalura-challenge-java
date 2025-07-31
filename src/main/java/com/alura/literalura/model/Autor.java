package com.alura.literalura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    /**
     * Identificador único do autor (chave primária).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do autor.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Ano de nascimento do autor.
     */
    @Column(name = "ano_nascimento", nullable = false)
    private Integer anoNascimento;

    /**
     * Ano de falecimento do autor (pode ser nulo se o autor estiver vivo).
     */
    @Column(name = "ano_falecimento")
    private Integer anoFalecimento;

    /**
     * Lista de livros escritos pelo autor.
     */
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Livro> livros;

    // Getters e Setters

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

    public Integer getAnoNascimento() {
        return anoNascimento;
    }

    public void setAnoNascimento(Integer anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public Integer getAnoFalecimento() {
        return anoFalecimento;
    }

    public void setAnoFalecimento(Integer anoFalecimento) {
        this.anoFalecimento = anoFalecimento;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    @Override
    public String toString() {
        String nascimentoStr = anoNascimento != null ? anoNascimento.toString() : "Desconhecido";
        String falecimentoStr = anoFalecimento != null ? anoFalecimento.toString() : "Desconhecido";
        String livrosStr = livros != null ? String.valueOf(livros.size()) : "Sem livros registrados";

        return "Autor: " + nome + "\n" +
                "Ano de nascimento: " + nascimentoStr + "\n" +
                "Ano de falecimento: " + falecimentoStr + "\n" +
                "Livros: " + livrosStr;
    }
}
