package splett.animal.tipo.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import splett.animal.tipo.TipoAnimal;
import splett.dao.GenericDao;

@ManagedBean(name = "tipoAnimalDao")
@ApplicationScoped
public class TipoAnimalDaoImpl extends GenericDao<TipoAnimal> implements TipoAnimalDao {
	private static final long serialVersionUID = 1L;

	public TipoAnimalDaoImpl() {
		super(TipoAnimal.class);
	}

	@SuppressWarnings("unchecked")
	public List<TipoAnimal> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select t from TipoAnimal t where lower(t.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		return q.getResultList();
	}
}
