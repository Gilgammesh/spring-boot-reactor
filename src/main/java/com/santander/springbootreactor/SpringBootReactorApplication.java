package com.santander.springbootreactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.santander.springbootreactor.model.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux
				.just("Andres Guzman", "Pedro Navaja", "Juan Perez", "Diego Flores", "Maria Larrea", "Camila Bazan",
						"Laura", "Cristina Mendoza", "Bruce Lee", "Bruce Willis")
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(),
						nombre.split(" ").length > 1 ? nombre.split(" ")[1].toUpperCase() : null))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
				.doOnNext(ele -> {
					if (ele == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					StringBuilder sb = new StringBuilder();
					sb.append(ele.getNombre()).append(" ").append(ele.getApellido());
					logger.info(sb.toString());
				})
				.map(usuario -> new Usuario(usuario.getNombre().toLowerCase(), usuario.getApellido().toLowerCase()));

		nombres.subscribe(usuario -> logger.info(usuario.toString()),
				error -> logger.error(error.getMessage()),
				() -> logger.info("Ha finalizado la ejecucion del observable con exito")

		);
	}

}
