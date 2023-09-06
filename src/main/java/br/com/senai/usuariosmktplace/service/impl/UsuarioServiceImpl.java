package br.com.senai.usuariosmktplace.service.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import br.com.senai.usuariosmktplace.entity.Usuario;
import br.com.senai.usuariosmktplace.repository.UsuarioRepository;
import br.com.senai.usuariosmktplace.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public Usuario salvar(String nome, String senha) {
		this.valida(nome, senha);
		String nomeFormatado = formatarNome(nome);
		String login = gerarLoginPor(nomeFormatado);
		String senhaHash = gerarHashDa(senha);
		Usuario novoUsuario = new Usuario(login, senhaHash, nomeFormatado);
		this.repository.save(novoUsuario);
		Usuario usuarioSalvo = this.repository.buscarPor(login);
		return usuarioSalvo;
	}

	@Override
	public Usuario buscarPor(String login) {
		Usuario usuarioEncontrado = this.repository.buscarPor(login);
		Preconditions.checkNotNull(usuarioEncontrado, "Não foi encontrado usuário vinculado ao login informado.");
		return usuarioEncontrado;
	}

	@Override
	public Usuario atualizarPor(String login, String nome, String senhaAntiga, String senhaNova) {
		this.valida(nome, senhaNova);
		Usuario usuarioExistente = repository.buscarPor(login);
		Preconditions.checkNotNull(usuarioExistente, "Não foi encontrado um usuário ao login informado.");

		String senhaAntigaCriptografada = gerarHashDa(senhaAntiga);
		boolean isSenhaValida = senhaAntigaCriptografada.equals(usuarioExistente.getSenha());
		Preconditions.checkArgument(isSenhaValida, "Senha antiga informada não corresponde ao usuário.");
		Preconditions.checkArgument(!senhaAntiga.equals(senhaNova), "A senha nova não pode ser igual a anterior.");

		String senhaNovaHash = gerarHashDa(senhaNova);
		usuarioExistente.setSenha(senhaNovaHash);
		usuarioExistente.setNome(nome);
		this.repository.atualizarPor(login, nome, senhaNovaHash);
		return usuarioExistente;
	}

	public String resetarSenha(String login) {
		Usuario usuarioExistente = repository.buscarPor(login);
		Preconditions.checkNotNull(usuarioExistente, "Usuário não encontrado.");
		String novaSenha = gerarSenhaRandom();
		String senhaNovaHash = gerarHashDa(novaSenha);
		usuarioExistente.setSenha(senhaNovaHash);
		this.repository.atualizarPor(login, senhaNovaHash);
		return novaSenha;
	}

	@SuppressWarnings("deprecation")
	private void valida(String senha) {
		boolean isSenhaValida = !Strings.isNullOrEmpty(senha) 
				&& senha.length() >= 6 && senha.length() <= 15;		
				
		Preconditions.checkArgument(isSenhaValida, "A senha é obrigatória "
				+ "deve conter entre 6 e 15 caracteres");
		boolean isContemLetra = CharMatcher.inRange('a', 'z').countIn(senha.toLowerCase()) > 0;
		boolean isContemNumero = CharMatcher.inRange('0', '9').countIn(senha) > 0;
		boolean isCaracterInvalido = !CharMatcher.javaLetterOrDigit().matchesAllOf(senha);

		Preconditions.checkArgument(isContemLetra && isContemNumero && !isCaracterInvalido,
				"A senha deve conter letras e numeros.");
	}

	private void valida(String nomeCompleto, String senha) {
		List<String> partesDoNome = fracionar(nomeCompleto);
		boolean isNomeCompleto = partesDoNome.size() > 1;
		boolean isNomeValido = !Strings.isNullOrEmpty(nomeCompleto) && isNomeCompleto;
		Preconditions.checkArgument(isNomeValido, "O nome é obrigatório e deve conter sobrenome também.");
		nomeCompleto = nomeCompleto.replaceAll("\\s+", "");
		this.valida(senha);
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
		if (!Strings.isNullOrEmpty(nomeCompleto)) {
			nomeCompleto = nomeCompleto.trim();
			String[] partesDoNome = nomeCompleto.split(" ");
			for (String parte : partesDoNome) {
				boolean isNaoContemArtigo = !parte.equalsIgnoreCase("de") && !parte.equalsIgnoreCase("e")
						&& !parte.equalsIgnoreCase("dos") && !parte.equalsIgnoreCase("da")
						&& !parte.equalsIgnoreCase("das");
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

				if (loginGerado.length() > 40) {
					loginGerado = loginGerado.substring(0, 40);
				}

				usuarioEncontrado = repository.buscarPor(loginGerado);

				if (usuarioEncontrado == null) {
					return loginGerado;
				}
			}
			int proximoSequencial = 0;
			String loginDisponivel = null;
			while (usuarioEncontrado != null) {
				loginDisponivel = loginGerado + ++proximoSequencial;
				usuarioEncontrado = repository.buscarPor(loginDisponivel);
			}
			loginGerado = loginDisponivel;
		}
		return loginGerado;
	}

	private String gerarHashDa(String senha) {
		return new DigestUtils(MessageDigestAlgorithms.SHA3_256).digestAsHex(senha);
	}

	private String formatarNome(String nomeCompleto) {
		String[] partesDoNome = nomeCompleto.trim().split(" ");
		StringBuilder nomeFormatado = new StringBuilder();
		for (String parte : partesDoNome) {
			if (!parte.isEmpty()) {
				nomeFormatado.append(Character.toUpperCase(parte.charAt(0))).append(parte.substring(1).toLowerCase())
						.append(" ");
			}
		}
		return nomeFormatado.toString().trim();
	}
}
