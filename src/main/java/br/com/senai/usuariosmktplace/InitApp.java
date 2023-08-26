package br.com.senai.usuariosmktplace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import br.com.senai.usuariosmktplace.core.service.UsuarioService;

@SpringBootApplication
public class InitApp {
	
	@Autowired
	private UsuarioService service;
	
	public static void main(String[] args) {
		SpringApplication.run(InitApp.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLinnerRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println(service.buscarUsuarioPor("alan.duarte").getNomeCompleto());
		};
	}

}
