package br.com.senai.usuariosmktplace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import br.com.senai.usuariosmktplace.entity.Usuario;
import br.com.senai.usuariosmktplace.service.impl.UsuarioServiceImpl;

@SpringBootApplication
public class InitApp {
	
	@Autowired
	private UsuarioServiceImpl usuarioService;

	public static void main(String[] args) {
		SpringApplication.run(InitApp.class, args);
	}

	@Bean
	public CommandLineRunner commandLinnerRunner(ApplicationContext ctx) {
		return args -> {

			Usuario usuarioSalvo = usuarioService.salvar("João da NUNES", "senha3456");
			System.out.println("Novo usuário salvo: " + usuarioSalvo);

		    // Exemplo de busca por login
		    String login = "joao.nunes";
		    Usuario usuarioEncontrado = usuarioService.buscarPor(login);
		    System.out.println("Usuário encontrado: " + usuarioEncontrado);

		    // Exemplo de atualização de senha e nome
		    String novoNome = "João nUNES Pereira";
		    String senhaAntiga = "senha3456";
		    String novaSenha = "novaSenha456";
		    Usuario usuarioAtualizado = usuarioService.atualizarPor(login, novoNome, senhaAntiga, novaSenha);
		    System.out.println("Usuário atualizado: " + usuarioAtualizado);

		    // Exemplo de redefinição de senha
		    String senhaResetada = usuarioService.resetarSenha(login);
		    System.out.println("Senha resetada: " + senhaResetada);
		};
	}

}
