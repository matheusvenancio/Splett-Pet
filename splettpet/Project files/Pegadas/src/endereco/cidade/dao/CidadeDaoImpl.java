package endereco.cidade.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import dao.GenericDao;
import endereco.cidade.Cidade;

@ManagedBean(name = "cidadeDao")
@ApplicationScoped
public class CidadeDaoImpl extends GenericDao<Cidade> implements CidadeDao {

	private static final long serialVersionUID = 1L;

	public CidadeDaoImpl() {
		super(Cidade.class);
	}

	@SuppressWarnings("unchecked")
	public List<Cidade> getCidadesByEstado(Integer id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select c from Cidade c where c.estado.id = :id");
		query.setParameter("id", id);
		return query.getResultList();
	}
}
