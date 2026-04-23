package learning.com.br.screenMatch;

import learning.com.br.screenMatch.models.DadosEpisodio;
import learning.com.br.screenMatch.models.DadosSerie;
import learning.com.br.screenMatch.models.DadosTemporada;
import learning.com.br.screenMatch.principal.Principal;
import learning.com.br.screenMatch.services.ConsumoApi;
import learning.com.br.screenMatch.services.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	private final Principal principal;

	public ScreenMatchApplication(Principal principal) {
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		principal.exibeMenu();
	}
}
