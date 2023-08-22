package br.com.senai.usuariosmktplace;

import br.com.senai.usuariosmktplace.core.service.UsuarioService;

public class InitApp {

	public static void main(String[] args) {
		System.out.println(new UsuarioService().criarUsuario("Eduarda Manenti", "dudinha123"));
		System.out.println(new UsuarioService().alterarUsuario("eduarda.manenti13", "Eduarda Manenti", "dudinha123", "olá14"));
	}

}
