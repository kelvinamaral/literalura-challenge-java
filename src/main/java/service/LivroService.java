package service;

import model.Livro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.LivroRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public List<Livro> listarLivros(){
        return livroRepository.findAll();
    }

    public List<Livro> listarLivrosPorIdioma(String idioma){
        return  livroRepository.findByIdioma(idioma);
    }

    public Livro criarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterLivroPorId(Long id){
        return livroRepository.findById(id);
    }

    public  Optional<Livro> obterLivroPorTitulo(String titulo){
        return livroRepository.findByTituloIgnoreCase(titulo);
    }

    public Livro atualizarLivro(Long id,Livro livroDetalhes){
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado!"));
        livro.setTitulo(livroDetalhes.getTitulo());
        livro.setIdioma(livroDetalhes.getIdioma());
        livro.setNumeroDownloads(livroDetalhes.getNumeroDownloads());
        livro.setAutor(livroDetalhes.getAutor());
        return livroRepository.save(livro);
    }

    public void deletarLivro(Long id) {
        livroRepository.deleteById(id);;
    }

}
