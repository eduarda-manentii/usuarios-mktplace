package br.com.senai.usuariosmktplace.core.dao;

import br.com.senai.usuariosmktplace.core.dao.postgres.DaoPostgresUsuario;

public class FactoryDao {
	
	private static FactoryDao instance;
	
	private FactoryDao() {}
	
	public DaoUsuario getDaoUsuario() {
		return new DaoPostgresUsuario();
	}
	
	
	public static FactoryDao getInstance() {
		if(instance == null) {
			instance = new FactoryDao();
		}
		return instance;
	}

}
