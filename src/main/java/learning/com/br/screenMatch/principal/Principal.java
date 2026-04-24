package learning.com.br.screenMatch.principal;

import learning.com.br.screenMatch.models.*;
import learning.com.br.screenMatch.services.ConsultaChatGPT;
import learning.com.br.screenMatch.services.ConsumoApi;
import learning.com.br.screenMatch.services.ConverteDados;
import learning.com.br.screenMatch.services.SerieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Principal {

    private Scanner sc = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final ConsumoApi consumo;
    private final ConverteDados conversor;
    private final ConsultaChatGPT chatGPT;
    private final SerieService serieService;
    private List<Serie> seriesList = new ArrayList<>();

    public Principal(ConsumoApi consumo, ConverteDados conversor, ConsultaChatGPT chatGPT,SerieService serieService) {
        this.consumo = consumo;
        this.conversor = conversor;
        this.chatGPT = chatGPT;
        this.serieService = serieService;
    }

    @Value("${omdb.api.key}")
    private String API_KEY;

    public void exibeMenu() {
        var opcao = -1;

        while(opcao!=0){
            var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Lista séries buscadas
                0 - Sair                                 
                """;

            System.out.println(menu);
            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1 -> buscarSerieWeb();
                case 2 -> buscarEpisodioPorSerie();
                case 3 -> listarSeriesBuscadas();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados,chatGPT.obterTraducao(dados.sinopse()));
        serieService.save(serie);
        System.out.println(serie);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = sc.nextLine();
        System.out.println(ENDERECO + nomeSerie.trim().replaceAll("\\s+", "+") + "&apikey=" + API_KEY);
        var json = consumo.obterDados(ENDERECO + nomeSerie.trim().replaceAll("\\s+", "+") + "&apikey=" + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie(){

        System.out.println("Series disponiveis: ");
        listarSeriesBuscadas();
        Serie serie = null;

        while (serie == null) {

            System.out.println("Escolha a serie: ");
            String serieName = sc.nextLine();

            Optional<Serie> optSerie = seriesList.stream().filter(x -> serieName.equalsIgnoreCase(x.getTitulo())).findAny();

            if (optSerie.isPresent()){

                List<DadosTemporada> dadosTemporadaList = new ArrayList<>();

                serie = optSerie.get();

                for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
                    var json = consumo.obterDados(ENDERECO + serie.getTitulo().trim().replaceAll("\\s+", "+") + "&season=" + i + "&apikey=" + API_KEY);
                    DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                    dadosTemporadaList.add(dadosTemporada);
                    }
                List<Episodio> episodioList = dadosTemporadaList.stream().flatMap(x-> x.episodios().stream()
                        .map(e-> new Episodio(x.numero(),e))).toList();


                serieService.salvarEpisodio(serie.getId(), episodioList);

                episodioList.forEach(System.out::println);

            } else{
                System.out.println("Série não disponivel. Tente novamente.");
            }

        }
    }

    private void listarSeriesBuscadas(){

        seriesList = serieService.findAllComEpisodios();
        seriesList.stream().sorted(Comparator.comparing(Serie::getGeneroPrincipal)).forEach(System.out::println);
    }
}
