package learning.com.br.screenMatch.services;

import jakarta.transaction.Transactional;
import learning.com.br.screenMatch.models.Episodio;
import learning.com.br.screenMatch.models.Serie;
import learning.com.br.screenMatch.repository.SerieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Transactional
    public void salvarEpisodio(Long serieId, List<Episodio> episodioList){
        Serie serie = repository.findById(serieId).orElseThrow();

        episodioList.forEach(serie::adicionarEpisodios);
    }
}
