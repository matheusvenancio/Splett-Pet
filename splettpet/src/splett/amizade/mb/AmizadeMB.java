package splett.amizade.mb;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.amizade.Amizade;
import splett.amizade.dao.AmizadeDao;
import splett.perfil.mb.PerfilMB;
import splett.session.SessionMB;
import splett.usuario.Usuario;
import splett.usuario.dao.UsuarioDao;

@ManagedBean(name = "amizadeMB")
@ViewScoped
public class AmizadeMB {
    @ManagedProperty(value = "#{sessionMB}")
    private SessionMB sessionMB;

    @ManagedProperty(value = "#{perfilMB}")
    private PerfilMB perfilMB;

    @ManagedProperty(value = "#{amizadeDao}")
    private AmizadeDao amizadeDao;

    @ManagedProperty(value = "#{usuarioDao}")
    private UsuarioDao usuarioDao;

    private List<Usuario> amigosUsuarioVisualizado;

    private List<Usuario> solicitacoesAmizadeUsuarioVisualizado;

    public void addFriend() {
	Amizade amizade = new Amizade();

	amizade.setUsuarioOrigem(sessionMB.getUsuarioLogado());
	amizade.setUsuarioDestino(perfilMB.getUsuarioVisualizado());

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

    public PerfilMB getPerfilMB() {
	return perfilMB;
    }

    public void setPerfilMB(PerfilMB perfilMB) {
	this.perfilMB = perfilMB;
    }

    public UsuarioDao getUsuarioDao() {
	return usuarioDao;
    }

    public void setUsuarioDao(UsuarioDao usuarioDao) {
	this.usuarioDao = usuarioDao;
    }

    public List<Usuario> getAmigosUsuarioVisualizado() {
	amigosUsuarioVisualizado = amizadeDao.listAmigos(perfilMB.getUsuarioVisualizado());
	return amigosUsuarioVisualizado;
    }

    public void setAmigosUsuarioVisualizado(List<Usuario> amigosUsuarioVisualizado) {
	this.amigosUsuarioVisualizado = amigosUsuarioVisualizado;
    }

    public List<Usuario> getSolicitacoesAmizadeUsuarioVisualizado() {
	solicitacoesAmizadeUsuarioVisualizado = amizadeDao
		.listSolicitacoes(perfilMB.getUsuarioVisualizado());
	return solicitacoesAmizadeUsuarioVisualizado;
    }

    public void setSolicitacoesAmizadeUsuarioVisualizado(
	    List<Usuario> solicitacoesAmizadeUsuarioVisualizado) {
	this.solicitacoesAmizadeUsuarioVisualizado = solicitacoesAmizadeUsuarioVisualizado;
    }
}
