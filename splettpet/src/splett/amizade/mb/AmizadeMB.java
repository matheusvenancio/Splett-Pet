package splett.amizade.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.amizade.Amizade;
import splett.amizade.dao.AmizadeDao;
import splett.session.SessionMB;
import splett.usuario.Usuario;
import splett.usuario.dao.UsuarioDao;

@ManagedBean(name = "amizadeMB")
@ViewScoped
public class AmizadeMB {
	@ManagedProperty(value = "#{sessionMB}")
	private SessionMB sessionMB;

	@ManagedProperty(value = "#{amizadeDao}")
	private AmizadeDao amizadeDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	public void addFriend() {
		Amizade amizade = new Amizade();

		Usuario novo = usuarioDao
				.findById(sessionMB.getUsuarioLogado().getId() + 1);

		amizade.setUsuarioOrigem(sessionMB.getUsuarioLogado());
		amizade.setUsuarioDestino(novo);

		amizadeDao.salvar(amizade);
	}

	public AmizadeDao getAmizadeDao() {
		return amizadeDao;
	}

	public void setAmizadeDao(AmizadeDao amizadeDao) {
		this.amizadeDao = amizadeDao;
	}

	public SessionMB getSessionMB() {
		return sessionMB;
	}

	public void setSessionMB(SessionMB sessionMB) {
		this.sessionMB = sessionMB;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}
}
