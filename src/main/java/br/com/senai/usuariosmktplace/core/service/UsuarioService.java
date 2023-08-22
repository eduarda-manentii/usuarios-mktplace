package br.com.senai.usuariosmktplace.core.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import br.com.senai.usuariosmktplace.core.dao.DaoUsuario;
import br.com.senai.usuariosmktplace.core.dao.FactoryDao;
import br.com.senai.usuariosmktplace.core.domain.Usuario;
import br.com.senai.usuariosmktplace.core.util.api.EnviarEmail;

public class UsuarioService {

	private DaoUsuario daoUsuario;

	public UsuarioService() {
		this.daoUsuario = FactoryDao.getInstance().getDaoUsuario();
	}

	public Usuario criarUsuario(String nomeCompleto, String senha) {
		if (validarNome(nomeCompleto) == false || validarSenha(senha) == false) {
			return null;
		} else {
			String login = gerarLoginPor(nomeCompleto);
			String senhaHash = gerarHashDa(senha);
			String nome = nomeCompleto;
			Usuario usuarioCadastrado = new Usuario(login, senhaHash, nome);
			this.daoUsuario.inserir(usuarioCadastrado);
			return usuarioCadastrado;
		}
	}

	public Usuario alterarUsuario(String login, String nomeCompleto, String senhaAntiga, String senhaNova) {
		if (validarNome(nomeCompleto) == false || validarSenha(senhaNova) == false) {
			return null;
		} else {
			Usuario usuarioExistente = buscarPor(login);
			if (usuarioExistente == null) {
				System.out.println("Usuário não encontrado.");
				return null;
			}

			String senhaAntigaHash = gerarHashDa(senhaAntiga);
			if (!usuarioExistente.getSenha().equals(senhaAntigaHash)) {
				System.out.println("Senha antiga informada não corresponde ao usuário.");
				return null;
			}

			String senhaNovaHash = gerarHashDa(senhaNova);
			usuarioExistente.setSenha(senhaNovaHash);
			usuarioExistente.setNomeCompleto(nomeCompleto);
			this.daoUsuario.alterar(usuarioExistente);
			System.out.println("Nome do usúario e senha alterados com sucesso!\n");
			String mensagem = "Uma senha foi alterada.";
			try {
				EnviarEmail.enviarEmail(mensagem);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return usuarioExistente;
		}
	}

	public String resetarSenha(String login) {
		Usuario usuarioExistente = buscarPor(login);
		if (usuarioExistente == null) {
			System.out.println("Usuário não encontrado.");
			return null;
		}
		String novaSenha = gerarSenhaRandom();
		String senhaNovaHash = gerarHashDa(novaSenha);
		usuarioExistente.setSenha(senhaNovaHash);
		this.daoUsuario.alterar(usuarioExistente);
		return novaSenha;
	}

	private String gerarSenhaRandom() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] randomBytes = new byte[5];
		secureRandom.nextBytes(randomBytes);
		String senhaAleatoria = new BigInteger(1, randomBytes).toString(16);
		return senhaAleatoria;
	}

	private String removerAcentoDo(String nomeCompleto) {
		return Normalizer.normalize(nomeCompleto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	private List<String> fracionar(String nomeCompleto) {
		List<String> nomeFracionado = new ArrayList<String>();
		nomeCompleto = nomeCompleto.trim();
		if (nomeCompleto != null && !nomeCompleto.isBlank()) {
			String[] partesDoNome = nomeCompleto.split(" ");
			for (String parte : partesDoNome) {
				boolean isNaoContemArtigo = !(parte.equalsIgnoreCase("de") || parte.equalsIgnoreCase("e")
						|| parte.equalsIgnoreCase("do") || parte.equalsIgnoreCase("dos") || parte.equalsIgnoreCase("da")
						|| parte.equalsIgnoreCase("das"));
				if (isNaoContemArtigo) {
					nomeFracionado.add(parte.toLowerCase());
				}
			}
		}
		return nomeFracionado;
	}

	private String gerarLoginPor(String nomeCompleto) {
		nomeCompleto = removerAcentoDo(nomeCompleto);
		List<String> partesDoNome = fracionar(nomeCompleto);
		String loginGerado = null;
		Usuario usuarioEncontrado = null;
		if (!partesDoNome.isEmpty()) {
			for (int i = 1; i < partesDoNome.size(); i++) {
				loginGerado = partesDoNome.get(0) + "." + partesDoNome.get(i);
				usuarioEncontrado = buscarPor(loginGerado);
				if (usuarioEncontrado == null) {
					return loginGerado;
				}
			}
			int proximoSequencial = 0;
			String loginDisponivel = null;
			while (usuarioEncontrado != null) {
				loginDisponivel = loginGerado + ++proximoSequencial;
				usuarioEncontrado = buscarPor(loginDisponivel);
			}
			loginGerado = loginDisponivel;
		}
		return loginGerado;
	}

	private String gerarHashDa(String senha) {
		return new DigestUtils(MessageDigestAlgorithms.MD5).digestAsHex(senha);
	}

	private boolean validarSenha(String senha) {
		if (senha == null || senha.isBlank()) {
			System.out.println("A senha é obrigatória.");
			return false;
		}
		return true;
	}

	private boolean validarNome(String nomeCompleto) {
		boolean isNomeInvalido = nomeCompleto == null || nomeCompleto.isBlank() || nomeCompleto.length() > 120
				|| nomeCompleto.length() < 5;

		if (isNomeInvalido) {
			System.out.println("O nome é obrigatório e deve conter sobrenome. Deve conter entre 5 a 50 caracteres.");
			return false;
		}

		List<String> partesDoNome = fracionar(nomeCompleto);
		if (partesDoNome.size() < 2) {
			System.out.println(("O nome deve conter tanto o nome quanto o sobrenome."));
			return false;
		}
		return true;
	}

	private Usuario buscarPor(String loginGerado) {
		Usuario encontrado = daoUsuario.buscarPor(loginGerado);
		return encontrado;
	}

}
