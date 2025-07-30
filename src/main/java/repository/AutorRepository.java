package repository;

import model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNome(String nome);

    @Query("SELECT a from Autor a LEFT JOIN FETCH a.livros WHERE (a.anoFalecimento IS NULL OR a.anoFalecimento >: ano) AND a.anoNascimento <= :ano")
    List<Autor> findAutoresVivosPorAnoComLivros(@Param("ano") int ano);

    @Query("SELECT a from Autor a LEFT JOIN FETCH a.livros")
    List<Autor> findAllComLivros();
}
