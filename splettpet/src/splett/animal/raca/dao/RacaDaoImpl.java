package splett.animal.raca.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import splett.animal.raca.Raca;
import splett.dao.GenericDao;

@ManagedBean(name = "racaDao")
@ApplicationScoped
public class RacaDaoImpl extends GenericDao<Raca> implements RacaDao {
	private static final long serialVersionUID = 1L;

	public RacaDaoImpl() {
		super(Raca.class);
	}

	@SuppressWarnings("unchecked")
	public List<Raca> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from Raca u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		return q.getResultList();
	}
}
