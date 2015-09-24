package doacao.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import dao.GenericDao;
import doacao.Doacao;

@ManagedBean(name = "doacaoDao")
@ApplicationScoped
public class DoacaoDaoImpl extends GenericDao<Doacao> implements DoacaoDao {
	private static final long serialVersionUID = 1L;

	public DoacaoDaoImpl() {
		super(Doacao.class);
	}

}
