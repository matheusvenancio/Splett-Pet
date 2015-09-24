package animal.raca.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import animal.raca.Raca;
import dao.GenericDao;

@ManagedBean(name = "racaDao")
@ApplicationScoped
public class RacaDaoImpl extends GenericDao<Raca> implements RacaDao {

	private static final long serialVersionUID = 1L;

	public RacaDaoImpl() {
		super(Raca.class);
	}

	@SuppressWarnings("unchecked")
	public List<Raca> getRacasByCategoria(Integer id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select r from Raca r where r.categoria.id = :id");
		query.setParameter("id", id);
		return (List<Raca>) query.getResultList();
	}

	
	@SuppressWarnings("unchecked")
	public List<Raca> listarRacasByCategoria(Integer id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select r from Raca r where r.categoria.id = :id");
		query.setParameter("id", id);
		return (List<Raca>) query.getResultList();
	}
}
