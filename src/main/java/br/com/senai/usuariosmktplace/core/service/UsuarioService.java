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
	
	public String removerAcentoDo(String nomeCompleto) {
		return Normalizer.normalize(nomeCompleto, 
				Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public List<String> fracionar(String nomeCompleto) {
		List<String> nomeFracionado = new ArrayList<String>();
		if (nomeCompleto != null && !nomeCompleto.isBlank()) {
			String[] partesDoNome = nomeCompleto.split(" ");
			for (String parte : partesDoNome) {
				boolean isNaoContemArtigo = !(parte.equalsIgnoreCase("de") 
						|| parte.equalsIgnoreCase("e")
						|| parte.equalsIgnoreCase("do")
						|| parte.equalsIgnoreCase("dos")
						|| parte.equalsIgnoreCase("da")
						|| parte.equalsIgnoreCase("das"));
				if (isNaoContemArtigo) {
					nomeFracionado.add(parte.toLowerCase());
				}
			}
		}
		return nomeFracionado;
	}
	
	public String gerarLoginPor(String nomeCompleto) {
		nomeCompleto = removerAcentoDo(nomeCompleto);
		List<String> partesDoNome = fracionar(nomeCompleto);
		String loginGerado = null;
		Usuario usuarioEncontrado = null;
		if(!partesDoNome.isEmpty()) {
			for (int i = 1; i < partesDoNome.size(); i++) {
				loginGerado = partesDoNome.get(0) + "." + partesDoNome.get(i);
				usuarioEncontrado = buscarPor(loginGerado);
				if (usuarioEncontrado == null) {
					return loginGerado;
				}
			}
			int proximoSequencial = 0;
			String loginDisponivel = null;
			while(usuarioEncontrado != null) {
				loginDisponivel = loginGerado +  ++proximoSequencial;
				usuarioEncontrado = buscarPor(loginDisponivel);
			}
			 loginGerado = loginDisponivel;
		}
			return loginGerado;
	}
	
	public String gerarHashDa(String senha) {
		return new DigestUtils(MessageDigestAlgorithms.MD5).digestAsHex(senha);
	}
	
	private Usuario buscarPor(String loginGerado) {
		Usuario encontrado = daoUsuario.buscarPor(loginGerado);
		return encontrado;
	}

}
