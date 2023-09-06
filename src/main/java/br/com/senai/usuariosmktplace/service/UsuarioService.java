package br.com.senai.usuariosmktplace.service;

import org.springframework.validation.annotation.Validated;

import br.com.senai.usuariosmktplace.entity.Usuario;
import jakarta.validation.constraints.NotNull;

@Validated
public interface UsuarioService {

	public Usuario salvar(@NotNull(message = "O nome é obrigatório.") String nome,
			@NotNull(message = "A senha é obrigatória.") String senha);

	public Usuario buscarPor(@NotNull(message = "O login é obrigatório.") String login);

	public Usuario atualizarPor(@NotNull(message = "O login é obrigatório.") String login,
			@NotNull(message = "O nome é obrigatório.") String nome,
			@NotNull(message = "A senha antiga é obrigatória.") String senhaAntiga,
			@NotNull(message = "A senha nova é obrigatória.") String senhaNova);

}