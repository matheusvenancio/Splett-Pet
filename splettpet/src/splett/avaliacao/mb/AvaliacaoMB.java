package splett.avaliacao.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.avaliacao.Avaliacao;
import splett.avaliacao.dao.AvaliacaoDao;
import splett.session.SessionMB;
import splett.usuario.Usuario;

@ManagedBean(name = "avaliacaoMB")
@ViewScoped
public class AvaliacaoMB {

	@ManagedProperty(value = "#{avaliacaoDao}")
	private AvaliacaoDao avaliacaoDao;

	@ManagedProperty(value = "#{avaliacaoLazyDataModel}")
	private AvaliacaoLazyDataModel avaliacaoLazyDataModel;

	@ManagedProperty(value = "#{sessionMB}")
	private SessionMB sessionMB;

	private List<Avaliacao> avaliacoesFiltered;

	private Avaliacao avaliacao;

	public AvaliacaoMB() {
		avaliacoesFiltered = new ArrayList<Avaliacao>();
	}

	public void criar() {
		avaliacao = new Avaliacao();
		avaliacao.setAvaliado(new Usuario());
		avaliacao.setAvaliador(new Usuario());
	}

	public void salvar() {
		if (avaliacao.getId() != null) {
			avaliacaoDao.update(avaliacao);
		} else {
			avaliacao.setAvaliador(sessionMB.getUsuarioLogado());
			avaliacao.setAvaliado(sessionMB.getUsuarioLogado());
			avaliacaoDao.salvar(avaliacao);
		}
	}

	public void remover() {
		avaliacaoDao.remover(avaliacao);
	}

	public void cancelar() {
		avaliacao = null;
	}

	public AvaliacaoDao getAvaliacaoDao() {
		return avaliacaoDao;
	}

	public void setAvaliacaoDao(AvaliacaoDao avaliacaoDao) {
		this.avaliacaoDao = avaliacaoDao;
	}

	public SessionMB getSessionMB() {
		return sessionMB;
	}
	
	public AvaliacaoLazyDataModel getAvaliacaoLazyDataModel() {
		return avaliacaoLazyDataModel;
	}

	public void setAvaliacaoLazyDataModel(
			AvaliacaoLazyDataModel avaliacaoLazyDataModel) {
		this.avaliacaoLazyDataModel = avaliacaoLazyDataModel;
	}

	public void setSessionMB(SessionMB sessionMB) {
		this.sessionMB = sessionMB;
	}

	public List<Avaliacao> getAvaliacoesFiltered() {
		return avaliacoesFiltered;
	}

	public void setAvaliacoesFiltered(List<Avaliacao> avaliacoesFiltered) {
		this.avaliacoesFiltered = avaliacoesFiltered;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

}
