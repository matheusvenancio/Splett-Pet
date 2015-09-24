package publicacao.finalFeliz.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import publicacao.finalFeliz.FinalFeliz;
import dao.GenericDao;

@ManagedBean(name = "finalFelizDao")
@ApplicationScoped
public class FinalFelizDaoImpl extends GenericDao<FinalFeliz> implements FinalFelizDao {
	private static final long serialVersionUID = 1L;
	
	public FinalFelizDaoImpl() {
		super(FinalFeliz.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FinalFeliz> listarFinaisFelizes() {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select p from Publicacao p join FinalFeliz f where p.isFinalFeliz = true order by f.dataAnuncio desc");
		return (List<FinalFeliz>) query.getResultList();
	}
}
