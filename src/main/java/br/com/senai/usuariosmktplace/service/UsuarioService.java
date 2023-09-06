package br.com.senai.usuariosmktplace.service;

import org.springframework.validation.annotation.Validated;

import br.com.senai.usuariosmktplace.entity.Usuario;
import jakarta.validation.constraints.NotNull;

@Validated
public interface UsuarioService {

	public Usuario salvar(@NotNull(message = "O usuário é obrigatório.") Usuario usuario);

	public Usuario buscarPor(@NotNull(message = "O login é obrigatório.") String login);

	public void atualizarPor(@NotNull(message = "O login é obrigatório.") String login,
			@NotNull(message = "A senha é obrigatória.") String senha,
			@NotNull(message = "O nome é obrigatório.") String nome);

}