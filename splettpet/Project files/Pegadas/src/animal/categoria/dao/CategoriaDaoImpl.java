package animal.categoria.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import animal.categoria.Categoria;
import dao.GenericDao;

@ManagedBean(name = "categoriaDao")
@ApplicationScoped
public class CategoriaDaoImpl extends GenericDao<Categoria> implements
		CategoriaDao {
	
	private static final long serialVersionUID = 1L;
	
	public CategoriaDaoImpl() {
		super(Categoria.class);
	}

	public Categoria findByNome(String nome) {
		Categoria categoria;
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select c from Categoria c where c.nome = :nome");
		query.setParameter("nome", nome);

		categoria = (Categoria) query.getSingleResult();
		return categoria;
	}
	
	public Categoria procurarPorId(int id) {
		Categoria categoria;
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select c from Categoria c where c.id = :id");
		query.setParameter("id", id);
		categoria = (Categoria) query.getSingleResult();
		return categoria;
	}
	
	@Override
	public Categoria findCategoriaById(Integer id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select c from Categoria c where c.id = :id)");
		query.setParameter("id", id);

		return (Categoria) query.getSingleResult();
	}

}
