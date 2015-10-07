package splett.postagem.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.postagem.Postagem;
import splett.postagem.dao.PostagemDao;
import splett.session.SessionMB;

@ManagedBean(name = "postagemMB")
@ViewScoped
public class PostagemMB {

	@ManagedProperty(value = "#{postagemDao}")
	private PostagemDao postagemDao;

	private Postagem postagem;


	@ManagedProperty(value = "#{sessionMB}")
	private SessionMB session;
	
	private List<Postagem> postagens;
	
	public PostagemMB() {
		postagens = new ArrayList<Postagem>();
		
	}
	
    @PostConstruct
    public void init() {
    	listarPostagens();
    }

	public void salvarPostagem(String texto){
		postagem.setDataPostagem(new Date());
		postagem.setUsuario(session.getUsuarioLogado());
		postagem.setTexto(texto);
		postagemDao.salvar(postagem);
	}
	
	public void criar() {
		postagem = new Postagem();
	}

	public void remover() {
		postagemDao.remover(postagem);
	}
	
	public void listarPostagens(){
		postagens = postagemDao.listarPostagens(session.getUsuarioLogado().getId());
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
		return session;
	}

	public void setSession(SessionMB session) {
		this.session = session;
	}

	public List<Postagem> getPostagens() {
		return postagens;
	}

	public void setPostagens(List<Postagem> postagens) {
		this.postagens = postagens;
	}
	
	
}
