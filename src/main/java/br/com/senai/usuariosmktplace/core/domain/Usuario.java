package br.com.senai.usuariosmktplace.core.domain;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class Usuario {

	@Getter @Setter
	private String login;
	
	@Getter @Setter
	private String senha;
	
	@Getter @Setter
	private String nomeCompleto;
	
	public Usuario(String login, String senha, String nomeCompleto) {
		this.login = login;
		this.senha = senha;
		this.nomeCompleto = nomeCompleto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(login);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(login, other.login);
	}
	
	@Override
	public String toString() {
		return "Nome do Usuario: " + getNomeCompleto() + "\nLogin do usuário: " + getLogin();
	}

}