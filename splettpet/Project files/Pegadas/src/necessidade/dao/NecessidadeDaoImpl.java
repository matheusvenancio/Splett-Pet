package necessidade.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import necessidade.Necessidade;
import animal.Animal;
import dao.GenericDao;

@ManagedBean(name = "necessidadeDao")
@ApplicationScoped
public class NecessidadeDaoImpl extends GenericDao<Necessidade> implements
		NecessidadeDao {
	private static final long serialVersionUID = 1L;

	public NecessidadeDaoImpl() {
		super(Necessidade.class);
	}

	@Override
	public Necessidade findNecessidadeById(int id) {
		try {
			
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select n from Necessidade n where n.id_publi = :id");
		query.setParameter("id", id);

		return (Necessidade) query.getSingleResult();

		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Animal> listarAnimaisNecessidade(int id) {
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select a from Necessidade n join n.animais a where n.id = (select n.id from Necessidade n where n.id = :id)");
			query.setParameter("id", id);
			return (List<Animal>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Necessidade> listarPrincipaisNecessidades() {
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select n from Necessidade n order by dataCadastro desc");
			return (List<Necessidade>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}
