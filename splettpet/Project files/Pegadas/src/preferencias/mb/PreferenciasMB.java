package preferencias.mb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import preferencias.Preferencias;
import preferencias.dao.PreferenciasDao;
import usuario.Usuario;
import usuario.dao.UsuarioDao;

@ManagedBean(name = "preferenciasMB")
@RequestScoped
public class PreferenciasMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cod;
	private Usuario usuario;
	private Preferencias preferencias;

	@ManagedProperty(value = "#{preferenciasDao}")
	private PreferenciasDao preferenciasDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	private boolean isConfirmada;
	private boolean isExistente;

	public void confirmacao() {
		try {
			usuario = preferenciasDao.getUsuarioByCod(cod);
			if (!usuario.getPreferencias().isContaConfirmada()) {
				usuario.getPreferencias().setContaConfirmada(true);
				usuarioDao.update(usuario);
				isExistente = true;
				isConfirmada = false;
			} else if (usuario.getPreferencias().isContaConfirmada()) {
				isExistente = true;
				isConfirmada = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isExistente = false;
			isConfirmada = false;
		}
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public PreferenciasDao getPreferenciasDao() {
		return preferenciasDao;
	}

	public void setPreferenciasDao(PreferenciasDao prefDao) {
		this.preferenciasDao = prefDao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public boolean isConfirmada() {
		return isConfirmada;
	}

	public void setConfirmada(boolean isConfirmada) {
		this.isConfirmada = isConfirmada;
	}

	public boolean isExistente() {
		return isExistente;
	}

	public void setExistente(boolean isExistente) {
		this.isExistente = isExistente;
	}

	public Preferencias getPreferencias() {
		return preferencias;
	}

	public void setPreferencias(Preferencias preferencias) {
		this.preferencias = preferencias;
	}

}
