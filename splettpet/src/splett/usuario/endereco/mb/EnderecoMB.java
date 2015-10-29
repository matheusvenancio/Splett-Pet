package splett.usuario.endereco.mb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.session.SessionMB;
import splett.usuario.Usuario;
import splett.usuario.dao.UsuarioDao;
import splett.usuario.endereco.Endereco;
import splett.usuario.endereco.dao.EnderecoDao;

@ManagedBean(name = "enderecoMB")
@ViewScoped
public class EnderecoMB {

	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	private Endereco endereco;

	private List<Usuario> usuarios;
	
	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;
	
    @ManagedProperty(value = "#{sessionMB}")
    private SessionMB sessionMB;

	public EnderecoMB() {
		usuarios = new ArrayList<Usuario>();
	}

	@PostConstruct
	public void criar() {
		endereco = new Endereco();
	}

	public void salvar() {
		if (endereco.getId() != null)
			enderecoDao.update(endereco);
		else
			enderecoDao.salvar(endereco);
	}

	public void remover() {
		enderecoDao.remover(endereco);
	}

	public void cancelar() {
		endereco = null;
	}

	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Usuario> getUsuarios() {
		usuarios = usuarioDao.pesquisarUsuarioPorCidade(endereco.getCidade(), sessionMB.getUsuarioLogado().getId());
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public SessionMB getSessionMB() {
		return sessionMB;
	}

	public void setSessionMB(SessionMB sessionMB) {
		this.sessionMB = sessionMB;
	}
	
	public boolean verficarListaUsuarios(){
		return (usuarios==null);
	}

}