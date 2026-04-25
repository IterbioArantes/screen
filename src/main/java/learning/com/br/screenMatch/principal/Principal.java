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
                4 - Buscar série por nome
                5 - Buscar série por ator
                6 - Top 5 séries
                7 - Buscar Serie por categoria
                0 - Sair                                 
                """;

            System.out.println(menu);
            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1 -> buscarSerieWeb();
                case 2 -> buscarEpisodioPorSerie();
                case 3 -> listarSeriesBuscadas();
                case 4 -> buscarSeriePorNome();
                case 5 -> buscarSeriePorAtor();
                case 6 -> buscarTopFiveSeries();
                case 7 -> buscarSeriePorCategoria();
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

            Optional<Serie> optSerie = seriesList.stream().filter(x -> serieName.equalsIgnoreCase(x.getTitulo())).findFirst();

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

    private void buscarSeriePorNome(){

        System.out.println("Escolha uma série pelo nome: ");
        String serieBuscada = sc.nextLine();
        Optional<Serie> serieEncontrada = serieService.findByTituloEqualsIgnoreCase(serieBuscada);

        if(serieEncontrada.isPresent()){
            System.out.println(serieEncontrada.get());
        }else{
            System.out.println("Série nao encontrada");
        }
    }

    private void buscarSeriePorAtor(){

        System.out.println("Escolha uma série pelo ator: ");
        String atorBuscado = sc.nextLine();

        List<Serie> serieEncontrada = serieService.findByAtoresContainingIgnoreCase(atorBuscado);

        if(!serieEncontrada.isEmpty()){
            serieEncontrada.forEach(x-> System.out.println("Ator: " + x.getAtores() + "Nome da Série: " + x.getTitulo() + " | Avaliação: " + x.getAvaliacao()));
        }else{
            System.out.println("Nenhuma série com esse ator");
        }
    }

    private void listarSeriesBuscadas(){

        seriesList = serieService.findAllComEpisodios();
        seriesList.stream().sorted(Comparator.comparing(Serie::getGeneroPrincipal)).forEach(System.out::println);
    }

    private void buscarTopFiveSeries(){

        List<Serie> serieTop = serieService.findTop5ByOrderByAvaliacaoDesc();

        serieTop.forEach(x-> System.out.println("Nome da Série: " + x.getTitulo() + " | Avaliação: " + x.getAvaliacao()));
    }

    private void buscarSeriePorCategoria(){

        System.out.println("Digite uma categoria: ");
        String categoriaBuscada = sc.nextLine();

        Categoria cat = Categoria.categoriaPrincipalPtbr(categoriaBuscada);

        if(cat != Categoria.NA){
            List<Serie> seriePorCategoria = serieService.findByGeneroPrincipal(cat);

            seriePorCategoria.forEach(x-> System.out.println("Nome da Série: " + x.getTitulo() + " | Avaliação: " + x.getGeneroPrincipal()));
        }else{
            System.out.println("Serie não encontrada para esta categoria");
        }
    }
}
