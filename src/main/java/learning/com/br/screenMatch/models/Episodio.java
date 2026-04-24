package learning.com.br.screenMatch.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
@Data
@NoArgsConstructor
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer temporada;
    private String titulo;
    private Integer numero;
    private Double avaliacao;
    private LocalDate dataLancamento;

    @ManyToOne
    @ToString.Exclude
    private Serie serie;

    public Episodio(Integer temporada, DadosEpisodio dadosEpisodio){
        this.temporada = temporada;
        this.titulo = dadosEpisodio.titulo();
        this.numero = dadosEpisodio.numero();
        try{
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch (NumberFormatException e){
            this.avaliacao = null;
        }
        try{
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        }catch (DateTimeParseException e){
            this.dataLancamento = null;
        }
    }
}