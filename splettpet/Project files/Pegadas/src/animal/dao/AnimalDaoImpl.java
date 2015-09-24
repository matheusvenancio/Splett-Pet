package animal.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import animal.Animal;
import dao.GenericDao;

@ManagedBean(name="animalDao")
@ApplicationScoped
public class AnimalDaoImpl extends GenericDao<Animal> implements AnimalDao {
	private static final long serialVersionUID = 1L;

	public AnimalDaoImpl() {
		super(Animal.class);
	}
	
	@Override
	public List<Animal> findAnimaisByCategoria(Integer id) {
		List<Animal> listaAnimaisPorCategoria;
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select a from Animal a inner join Categoria c on a.id_animal = c.id_categoria where c.id_categoria = :id");
		query.setParameter("id", id);
		listaAnimaisPorCategoria = query.getResultList();
		return listaAnimaisPorCategoria;
	}
}
