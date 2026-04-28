package learning.com.br.screenMatch.repository;

import learning.com.br.screenMatch.models.Categoria;
import learning.com.br.screenMatch.models.Episodio;
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

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGeneroPrincipal(Categoria categoriaBuscada);

    @Query("SELECT e FROM Serie s JOIN s.episodioList e WHERE e.titulo ILIKE %:trechoEp%")
    List<Episodio> findEpByString(String trechoEp);

    @Query("SELECT e FROM Serie s JOIN s.episodioList e WHERE s=:serie AND e.avaliacao IS NOT null ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topFiveEpPorSerie(Serie serie);
}

