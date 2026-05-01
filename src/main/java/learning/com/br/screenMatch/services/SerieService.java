package learning.com.br.screenMatch.services;

import org.springframework.transaction.annotation.Transactional;
import learning.com.br.screenMatch.dto.EpisodioDTO;
import learning.com.br.screenMatch.dto.SerieDTO;
import learning.com.br.screenMatch.models.Categoria;
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

    public List<SerieDTO> findAll() {

        return repository.findAll().stream().map(this::toDto).toList();
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

    public List<SerieDTO> findTop5ByOrderByAvaliacaoDesc() {

        return repository.findTop5ByOrderByAvaliacaoDesc().stream().map(this::toDto).toList();
    }

    public List<Serie> findByGeneroPrincipal(Categoria categoriaBuscada) {
        return repository.findByGeneroPrincipal(categoriaBuscada);
    }


    public List<Episodio> findEpByString(String trechoEp) {
        return repository.findEpByString(trechoEp);


    }

    public List<Episodio> top5EpPorSerie(Serie serie) {
        return repository.top5EpPorSerie(serie);
    }

    public List<SerieDTO> top5SeriesPorLancamento(){
        return repository.top5SeriesPorLancamento().stream().map(this::toDto).toList();
    }

    public SerieDTO obterPorId(Long id) {
        return toDto(repository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public List<EpisodioDTO> obterEpsSerie(Long id) {
        Serie serie = repository.findById(id).orElseThrow();

        return serie.getEpisodioList().stream()
                .map(e-> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumero()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EpisodioDTO> obterTemporadaSerie(Long id, Integer temp) {
        List<Episodio> episodioList= repository.obterTemporadaSerie(id, temp);
        return episodioList.stream()
                .map(e-> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumero()))
                .toList();
    }

    public List<SerieDTO> seriesPorCateggoria(String genero) {
        Categoria categoria = Categoria.categoriaPrincipalPtbr(genero);
        return repository.findByGeneroPrincipal(categoria).stream()
                .map(this::toDto).toList();
    }


    private SerieDTO toDto(Serie serie){
        return new SerieDTO(serie.getId(),
                serie.getTitulo(),
                serie.getGeneroPrincipal(),
                serie.getTotalTemporadas(),
                serie.getAvaliacao(),
                serie.getAtores(),
                serie.getPoster(),
                serie.getSinopse());
    }


}