package learning.com.br.screenMatch.models;

import jakarta.persistence.*;
import learning.com.br.screenMatch.services.ConsultaChatGPT;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "series")
@Data
@NoArgsConstructor
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Categoria generoPrincipal;
    private Integer totalTemporadas;
    private Double avaliacao;
    private String atores;
    private String poster;
    private String sinopse;

    @Transient
    private List<Episodio> episodioList = new ArrayList<>();

    public Serie(DadosSerie dadosSerie, String sinopse) {

        this.generoPrincipal = Categoria.categoriaPrincipal(dadosSerie.genero());
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = parseDoubleOrNull(dadosSerie.avaliacao());
        this.poster = dadosSerie.poster();
        this.atores = dadosSerie.atores();
        this.sinopse = sinopse;
    }

    private Double parseDoubleOrNull(String valor) {
        try {
            return Double.valueOf(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}