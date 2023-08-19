package br.com.senai.usuariosmktplace;

import br.com.senai.usuariosmktplace.core.service.UsuarioService;

public class InitApp {

	public static void main(String[] args) {
		System.out.println(new UsuarioService().fracionar("Gábriél dos DA Dos de Santos e seila"));
		System.out.println(new UsuarioService().gerarLoginPor("Jóse da Silva ANJOS"));
		System.out.println(new UsuarioService().gerarHashDa("4547"));
	}

}
