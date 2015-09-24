package splett.session;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import splett.amizade.Amizade;
import splett.amizade.dao.AmizadeDao;
import splett.amizade.dao.AmizadeDaoImpl;
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
		usuarioVisualizado = usuarioLogado;
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

	public void addFriend() {
		Amizade amizade = new Amizade();

		amizade.setUsuarioOrigem(usuarioLogado);
		amizade.setUsuarioDestino(usuarioVisualizado);

		AmizadeDao dao = new AmizadeDaoImpl();
		dao.salvar(amizade);
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

	public boolean isFriendshipRequestAllowed() {
		return !isSelfProfile();
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
