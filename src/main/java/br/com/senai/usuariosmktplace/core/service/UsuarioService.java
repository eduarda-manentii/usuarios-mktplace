package br.com.senai.usuariosmktplace.core.service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import br.com.senai.usuariosmktplace.core.dao.DaoUsuario;
import br.com.senai.usuariosmktplace.core.dao.FactoryDao;
import br.com.senai.usuariosmktplace.core.domain.Usuario;

public class UsuarioService {

	private DaoUsuario daoUsuario;

	public UsuarioService() {
		this.daoUsuario = FactoryDao.getInstance().getDaoUsuario();
	}

	public Usuario criarUsuario(String nomeCompleto, String senha) {
	    if (validarNome(nomeCompleto) == false || validarSenha(senha) == false) {
	        return null;
	    } else {
	        String nomeFormatado = formatarNome(nomeCompleto);
	        String login = gerarLoginPor(nomeFormatado);
	        String senhaHash = gerarHashDa(senha);
	        Usuario usuarioCadastrado = new Usuario(login, senhaHash, nomeFormatado);
	        this.daoUsuario.inserir(usuarioCadastrado);
	        return usuarioCadastrado;
	    }
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
	
	private String formatarNome(String nomeCompleto) {
	    String[] partesDoNome = nomeCompleto.trim().split(" ");
	    StringBuilder nomeFormatado = new StringBuilder();

	    for (String parte : partesDoNome) {
	        if (!parte.isEmpty()) {
	            nomeFormatado.append(Character.toUpperCase(parte.charAt(0)))
	                        .append(parte.substring(1).toLowerCase()).append(" ");
	        }
	    }

	    return nomeFormatado.toString().trim();
	}

	private boolean validarSenha(String senha) {
	    if (senha == null || senha.isBlank()) {
	        System.out.println("A senha é obrigatória.");
	        return false;
	    }

	    if (senha.length() < 6 || senha.length() > 15) {
	        System.out.println("A senha deve ter entre 6 e 15 caracteres.");
	        return false;
	    }

	    if (senha.matches("\\s+")) {
	        System.out.println("A senha não pode conter espaços em branco.");
	        return false;
	    }

	    if (!senha.matches(".*\\d.*")) {
	        System.out.println("A senha deve conter pelo menos um número.");
	        return false;
	    }

	    if (!senha.matches(".*[a-zA-Z].*")) {
	        System.out.println("A senha deve conter pelo menos uma letra.");
	        return false;
	    }

	    return true;
	}

	private boolean validarNome(String nomeCompleto) {
	    nomeCompleto = nomeCompleto.replaceAll("\\s+", ""); 
	    boolean isNomeInvalido = nomeCompleto == null || nomeCompleto.isEmpty() || nomeCompleto.length() > 120
	            || nomeCompleto.length() < 5;

	    if (isNomeInvalido) {
	        System.out.println("O nome deve conter entre 5 a 50 caracteres.");
	        return false;
	    }

	    List<String> partesDoNome = fracionar(nomeCompleto);
	    if (partesDoNome.size() < 1) {
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
