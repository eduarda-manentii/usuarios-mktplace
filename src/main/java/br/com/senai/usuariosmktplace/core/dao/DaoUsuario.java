package br.com.senai.usuariosmktplace.core.dao;

import br.com.senai.usuariosmktplace.core.domain.Usuario;

public interface DaoUsuario {
	
	public void inserir(Usuario usuario);
	public void alterar(Usuario usuario);
	public Usuario buscarPor(String login);

}