package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alura.literalura.repository.AutorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarAutores(){
        return autorRepository.findAllComLivros();
    }

    public List<Autor> listarAutoresVivosPorAno(int ano){
        return autorRepository.findAutoresVivosPorAnoComLivros(ano);
    }

    public Autor criarAutor(Autor autor){
        return autorRepository.save(autor);
    }

    public Optional<Autor> obterAutorPorId(Long id){
        return autorRepository.findById(id);
    }

    public Optional<Autor> obterAutorPorNome(String nome){
        return autorRepository.findByNome(nome);
    }

    public Autor atualizarAutor(Long id, Autor autorDetalhes){
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado!"));
        autor.setNome(autorDetalhes.getNome());
        autor.setAnoNascimento(autorDetalhes.getAnoNascimento());
        autor.setAnoFalecimento(autorDetalhes.getAnoFalecimento());
        return autorRepository.save(autor);
    }

    public void removerAutor(Long id){
        autorRepository.deleteById(id);
    }
}
