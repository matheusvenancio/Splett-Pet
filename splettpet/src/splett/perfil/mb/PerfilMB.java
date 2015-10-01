package splett.perfil.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.session.SessionMB;
import splett.usuario.dao.UsuarioDao;

@ManagedBean(name = "perfilMB")
@ViewScoped
public class PerfilMB {
    @ManagedProperty(value = "#{sessionMB}")
    private SessionMB sessionMB;

    @ManagedProperty(value = "#{usuarioDao}")
    private UsuarioDao usuarioDao;

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

    public boolean isFriendshipRequestAllowed() {
	return !sessionMB.isSelfProfile();
    }

}
