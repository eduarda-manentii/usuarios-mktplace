package br.com.senai.usuariosmktplace;

import br.com.senai.usuariosmktplace.core.dao.ManagerDb;

public class InitApp {

	public static void main(String[] args) {
		ManagerDb.getInstance().getConexao();
		System.out.println("Conectou ao banco.");
	}

}
