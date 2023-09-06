package br.com.senai.usuariosmktplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.senai.usuariosmktplace.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	@Modifying
	@Query(value = "UPDATE Usuario u SET u.nome = :nome, u.senha = :senha WHERE u.login = :login")
	public void atualizarPor(String login, String nome, String senha);
	
	@Query(value = "SELECT u FROM Usuario u WHERE Upper(u.login) = Upper(:login)")
	public Usuario buscarPor(String login);

}