package br.com.senai.usuariosmktplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.senai.usuariosmktplace.entity.Usuario;
import jakarta.transaction.Transactional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Modifying @Transactional
	@Query(value = "UPDATE Usuario u SET u.nome = :nome, u.senha = :senha WHERE u.login = :login")
	public void atualizarPor(@Param("login") String login, @Param("nome")String nome, 
			@Param("senha") String senha);

	@Modifying @Transactional
	@Query(value = "UPDATE Usuario u SET u.senha = :senha WHERE u.login = :login")
	public void atualizarPor(@Param("login") String login, @Param("senha") String senha);

	@Query(value = "SELECT u FROM Usuario u WHERE Upper(u.login) = Upper(:login)")
	public Usuario buscarPor(@Param("login") String login);

}