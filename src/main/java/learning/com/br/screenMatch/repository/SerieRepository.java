package learning.com.br.screenMatch.repository;

import learning.com.br.screenMatch.models.Serie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {

    @Query("SELECT DISTINCT s FROM Serie s LEFT JOIN FETCH s.episodioList")
    List<Serie> findAllComEpisodios();

    @EntityGraph(attributePaths = "episodioList")
    Optional<Serie> findByTituloEqualsIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);
}

