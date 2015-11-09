package splett.configuracoes.mb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.criptografia.Criptografia;
import splett.endereco.Logradouro;
import splett.perfil.mb.PerfilMB;
import splett.usuario.Usuario;
import splett.usuario.dao.UsuarioDao;
import splett.usuario.endereco.dao.EnderecoDao;

@ManagedBean(name = "configuracoesMB")
@ViewScoped
public class ConfiguracoesMB {

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{perfilMB}")
	private PerfilMB perfilMB;
	
	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;
	
	@ManagedProperty(value = "#{criptografia}")
	private Criptografia criptografia;
	
	private String senha = "";
	
	private Usuario usuario;

	@PostConstruct
	public void criar() {
		usuario = new Usuario();
		usuario = perfilMB.getUsuarioVisualizado();
	}

	public void procurarEndereco() {
		List<Logradouro> enderecos = enderecoDao.pesquisarPorCep(usuario
				.getEndereco().getCep());
		Logradouro l = enderecos.get(0);
		usuario.getEndereco().setLogradouro(l.getNome());
		usuario.getEndereco().setBairro(l.getBairroInicial().getNome());
		usuario.getEndereco().setCidade(l.getLocalidade().getNome());
		usuario.getEndereco().setUf(l.getUf());
		
	}

	public void salvar() {
		usuario.setSenha(criptografia.criptografar(senha));
		usuarioDao.update(usuario);
	}

	public void cancelar() {
		usuario = null;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}
	
	

	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

	public Criptografia getCriptografia() {
		return criptografia;
	}

	public void setCriptografia(Criptografia criptografia) {
		this.criptografia = criptografia;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public PerfilMB getPerfilMB() {
		return perfilMB;
	}

	public void setPerfilMB(PerfilMB perfilMB) {
		this.perfilMB = perfilMB;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
