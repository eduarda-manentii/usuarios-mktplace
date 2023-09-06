package br.com.senai.usuariosmktplace.service.impl;

import br.com.senai.usuariosmktplace.entity.Usuario;
import br.com.senai.usuariosmktplace.service.UsuarioService;
import jakarta.validation.constraints.NotNull;

public class UsuarioServiceImpl implements UsuarioService {

	@Override
	public Usuario salvar(@NotNull(message = "O usuário é obrigatório.") Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario buscarPor(@NotNull(message = "O login é obrigatório.") String login) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void atualizarPor(@NotNull(message = "O login é obrigatório.") String login,
			@NotNull(message = "A senha é obrigatória.") String senha,
			@NotNull(message = "O nome é obrigatório.") String nome) {
		// TODO Auto-generated method stub

	}

}
