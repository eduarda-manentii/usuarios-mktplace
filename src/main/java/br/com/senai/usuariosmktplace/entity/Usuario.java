package br.com.senai.usuariosmktplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor @NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "usuarios")
@Entity(name = "Usuario")
public class Usuario {
	
	@Id
	@NotBlank(message = "O login do usuário é obrigatório.")
	@Column(name = "login")
	@EqualsAndHashCode.Include
	private String login;
	
	@NotBlank(message = "A senha é obrigatória.")
	@Size(min = 6, max = 15, message = "A senha deve conter entre 6 a 15 caracteres.")
	@Column(name = "senha")
	private String senha;
	
	@NotBlank(message = "O nome do usuário é obrigatório.")
	@Size(min = 5, max = 50, message = "O nome deve conter entre 5 a 50 caracteres.")
	@Column(name = "nome")
	@ToString.Include
	private String nome;

}
