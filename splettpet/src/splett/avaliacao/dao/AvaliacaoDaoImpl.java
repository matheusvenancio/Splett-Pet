package splett.avaliacao.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import splett.avaliacao.Avaliacao;
import splett.dao.GenericDao;

@ManagedBean(name = "avaliacaoDao")
@ApplicationScoped
public class AvaliacaoDaoImpl extends GenericDao<Avaliacao> implements AvaliacaoDao  {

	private static final long serialVersionUID = 1L;

	public AvaliacaoDaoImpl() {
		super(Avaliacao.class);
	}

}
