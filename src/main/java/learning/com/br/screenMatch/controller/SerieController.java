package learning.com.br.screenMatch.controller;

import learning.com.br.screenMatch.dto.EpisodioDTO;
import learning.com.br.screenMatch.dto.SerieDTO;
import learning.com.br.screenMatch.services.SerieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
@RequiredArgsConstructor
public class SerieController {

    private final SerieService serieService;

    @GetMapping
    public List<SerieDTO> obterSeries(){
         return serieService.findAll();
    }

    @GetMapping("/top5")
    public List<SerieDTO> buscarTop5SeriesPorAvaliacaoDesc(){
        return serieService.findTop5ByOrderByAvaliacaoDesc();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> top5SeriesPorLancamento(){
        return serieService.top5SeriesPorLancamento();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id){
        return serieService.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterEpsSerie(@PathVariable Long id){
        return serieService.obterEpsSerie(id);
    }

    @GetMapping("/{id}/temporadas/{temp}")
    public List<EpisodioDTO> obterEpsSerie(@PathVariable Long id, @PathVariable Integer temp){
        return serieService.obterTemporadaSerie(id, temp);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDTO> seriesPorCateggoria(@PathVariable String genero){
        return serieService.seriesPorCateggoria(genero);
    }
}
