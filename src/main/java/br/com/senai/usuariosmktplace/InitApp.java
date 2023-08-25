package br.com.senai.usuariosmktplace;

import br.com.senai.usuariosmktplace.core.domain.Usuario;

public class InitApp {

	public static void main(String[] args) {
		Usuario u  = new Usuario("jose.silva", "Jose manenti", "jose43540");
		u.getLogin();
	}
	
}
