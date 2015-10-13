package splett.session;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import splett.usuario.TipoUsuario;
import splett.usuario.Usuario;
import splett.usuario.dao.UsuarioDao;

@ManagedBean(name = "sessionMB")
@SessionScoped
public class SessionMB {

	@ManagedProperty(value = "#{usuarioLogado}")
	private Usuario usuarioLogado;

	@ManagedProperty(value = "#{usuarioVisualizado}")
	private Usuario usuarioVisualizado;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@PostConstruct
	public void init() {
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		usuarioLogado = usuarioDao.pesquisarPorEmail(user.getUsername());
		usuarioVisualizado = usuarioDao.findById(usuarioLogado.getId() + 1);
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public boolean isLogado() {
		if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return true;
	}

	public void returnToSelfProfile() {
		usuarioVisualizado = usuarioLogado;
	}

	public boolean isSelfProfile() {
		return usuarioVisualizado == usuarioLogado;
	}

	public boolean isUserAdm() {
		return usuarioLogado.getTipo().equals(TipoUsuario.ROLE_ADMIN);
	}

	public boolean isManagementAllowed() {
		return isSelfProfile() || isUserAdm();
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuarioVisualizado() {
		return usuarioVisualizado;
	}

	public void setUsuarioVisualizado(Usuario usuarioVisualizado) {
		this.usuarioVisualizado = usuarioVisualizado;
	}

}
