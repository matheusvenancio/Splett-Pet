package splett.animal.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import splett.animal.Animal;
import splett.dao.GenericDao;

@ManagedBean(name = "animalDao")
@ApplicationScoped
public class AnimalDaoImpl extends GenericDao<Animal> implements AnimalDao {
	private static final long serialVersionUID = 1L;

	public AnimalDaoImpl() {
		super(Animal.class);

	}

	@SuppressWarnings("unchecked")
	public List<Animal> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select a from Animal a where lower(a.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		return q.getResultList();
	}
}
