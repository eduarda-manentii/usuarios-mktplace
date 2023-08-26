package br.com.senai.usuariosmktplace.core.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import br.com.senai.usuariosmktplace.core.dao.postgres.DaoPostgresUsuario;

@Service
public class FactoryDao {

	@Bean
	public DaoUsuario getDaoUsuario() {
		return new DaoPostgresUsuario();
	}

}
