package splett.amizade.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import splett.amizade.Amizade;
import splett.dao.GenericDao;

@ManagedBean(name = "amizadeDao")
@ApplicationScoped
public class AmizadeDaoImpl extends GenericDao<Amizade> implements AmizadeDao{

	private static final long serialVersionUID = 1L;

	public AmizadeDaoImpl() {
		super(Amizade.class);
	}
}
