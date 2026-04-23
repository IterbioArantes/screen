package learning.com.br.screenMatch.principal;

import learning.com.br.screenMatch.models.*;
import learning.com.br.screenMatch.repository.ServiceRepository;
import learning.com.br.screenMatch.services.ConsultaChatGPT;
import learning.com.br.screenMatch.services.ConsumoApi;
import learning.com.br.screenMatch.services.ConverteDados;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final ConsumoApi consumo;
    private final ConverteDados conversor;
    private final ConsultaChatGPT chatGPT;
    private final ServiceRepository serviceRepository;

    public Principal(ConsumoApi consumo, ConverteDados conversor, ConsultaChatGPT chatGPT,ServiceRepository serviceRepository) {
        this.consumo = consumo;
        this.conversor = conversor;
        this.chatGPT = chatGPT;
        this.serviceRepository = serviceRepository;
    }

    @Value("${omdb.api.key}")
    private String API_KEY;

    private final List<Serie> seriesList = new ArrayList<>();


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
            opcao = Integer.parseInt(leitura.nextLine());

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
        seriesList.add(serie);
        serviceRepository.save(serie);
        System.out.println(serie);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        System.out.println(ENDERECO + nomeSerie.trim().replaceAll("\\s+", "+") + "&apikey=" + API_KEY);
        var json = consumo.obterDados(ENDERECO + nomeSerie.trim().replaceAll("\\s+", "+") + "&apikey=" + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie(){
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().trim().replaceAll("\\s+", "+") + "&season=" + i + "&apikey=" + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas(){

        seriesList.stream().sorted(Comparator.comparing(Serie::getGeneroPrincipal)).forEach(System.out::println);
    }
}
