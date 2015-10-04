package splett.avaliacao.mb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import splett.avaliacao.Avaliacao;
import splett.avaliacao.dao.AvaliacaoDao;
import splett.avaliacao.mb.LazyAvaliacaoSorter;
import splett.session.SessionMB;
import splett.usuario.Usuario;

@ManagedBean(name = "avaliacaoLazyDataModel")
@ViewScoped
public class AvaliacaoLazyDataModel extends LazyDataModel<Avaliacao> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{avaliacaoDao}")
	private AvaliacaoDao avaliacaoDao;

	@ManagedProperty(value = "#{sessionMB}")
	private SessionMB sessionMB;

	@Override
	public List<Avaliacao> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Avaliacao> source = null;

		Usuario u = new Usuario();
		u = sessionMB.getUsuarioVisualizado();

		source = avaliacaoDao.listAvaliacoes(u.getId());

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyAvaliacaoSorter(sortField,
					sortOrder));
		}

		// rowCount
		this.setRowCount(avaliacaoDao.getRowCount());

		return source;
	}

	@Override
	public Avaliacao getRowData(String rowKey) {
		return avaliacaoDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Avaliacao avaliacao) {
		return avaliacao.getId();
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

	public void setSessionMB(SessionMB sessionMB) {
		this.sessionMB = sessionMB;
	}

}
