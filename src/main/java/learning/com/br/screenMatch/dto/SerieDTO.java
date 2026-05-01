package learning.com.br.screenMatch.dto;

import learning.com.br.screenMatch.models.Categoria;

public record SerieDTO(Long id,
        String titulo,
        Categoria generoPrincipal,
        Integer totalTemporadas,
        Double avaliacao,
        String atores,
        String poster,
        String sinopse) {
}