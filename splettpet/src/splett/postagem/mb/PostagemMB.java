package splett.postagem.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.perfil.mb.PerfilMB;
import splett.postagem.Postagem;
import splett.postagem.dao.PostagemDao;
import splett.session.SessionMB;

@ManagedBean(name = "postagemMB")
@ViewScoped
public class PostagemMB {

    @ManagedProperty(value = "#{postagemDao}")
    private PostagemDao postagemDao;

    private Postagem postagem;

    private Postagem postagemVisualizar;

    @ManagedProperty(value = "#{sessionMB}")
    private SessionMB sessionMB;

    @ManagedProperty(value = "#{perfilMB}")
    private PerfilMB perfilMB;

    private List<Postagem> postagens;

    public PostagemMB() {
	postagens = new ArrayList<Postagem>();
    }

    @PostConstruct
    public void init() {
	listarPostagens();
    }

    public String salvarPostagem() {
	postagem.setDataPostagem(new Date());
	postagem.setUsuario(sessionMB.getUsuarioLogado());
	postagemDao.salvar(postagem);
	listarPostagens();
	return "postagens";
    }

    public void setarPostagem(Integer id) {
	postagemVisualizar = postagemDao.findByIdInteger(id);
    }

    public void criar() {
	postagem = new Postagem();
    }

    public void remover() {
	postagemDao.remover(postagem);
    }

    public void listarPostagens() {
	postagens = postagemDao.listarPostagens(perfilMB.getUsuarioVisualizado().getId());
    }

    public void cancelar() {
	postagem = null;
    }

    public Postagem getPostagem() {
	return postagem;
    }

    public void setPostagem(Postagem postagem) {
	this.postagem = postagem;
    }

    public PostagemDao getPostagemDao() {
	return postagemDao;
    }

    public void setPostagemDao(PostagemDao postagemDao) {
	this.postagemDao = postagemDao;
    }

    public SessionMB getSession() {
	return sessionMB;
    }

    public void setSession(SessionMB sessionMB) {
	this.sessionMB = sessionMB;
    }

    public List<Postagem> getPostagens() {
	return postagens;
    }

    public void setPostagens(List<Postagem> postagens) {
	this.postagens = postagens;
    }

    public Postagem getPostagemVisualizar() {
	return postagemVisualizar;
    }

    public void setPostagemVisualizar(Postagem postagemVisualizar) {
	this.postagemVisualizar = postagemVisualizar;
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

}
