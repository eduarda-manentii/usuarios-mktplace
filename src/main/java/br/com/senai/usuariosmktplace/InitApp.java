package br.com.senai.usuariosmktplace;

import br.com.senai.usuariosmktplace.core.service.UsuarioService;

public class InitApp {

	public static void main(String[] args) {
		UsuarioService u = new UsuarioService();
		u.criarUsuario("eduarda manenti", "duda123");
	}

}
