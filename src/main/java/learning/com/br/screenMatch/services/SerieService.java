package learning.com.br.screenMatch.services;

import jakarta.transaction.Transactional;
import learning.com.br.screenMatch.models.Episodio;
import learning.com.br.screenMatch.models.Serie;
import learning.com.br.screenMatch.repository.SerieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SerieService {

    private final SerieRepository repository;

    public Serie save(Serie serie) {
        return repository.save(serie);
    }

    public List<Serie> findAll() {
        return repository.findAll();
    }

    public List<Serie> findAllComEpisodios() {
        return repository.findAllComEpisodios();
    }

    public Optional<Serie> findByTituloEqualsIgnoreCase(String nomeSerie){
        return repository.findByTituloEqualsIgnoreCase(nomeSerie);
    }

    public List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor){
        return repository.findByAtoresContainingIgnoreCase(nomeAtor);
    }

    @Transactional
    public void salvarEpisodio(Long serieId, List<Episodio> episodioList){
        Serie serie = repository.findById(serieId).orElseThrow();

        episodioList.forEach(serie::adicionarEpisodios);
    }
}
