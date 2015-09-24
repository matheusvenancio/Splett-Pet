package evento.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import animal.Animal;

import dao.GenericDao;
import evento.Evento;

@ManagedBean(name="eventoDao")
@ApplicationScoped
public class EventoDaoImpl extends GenericDao<Evento> implements EventoDao {
	private static final long serialVersionUID = 1L;

	public EventoDaoImpl() {
		super(Evento.class);
	}

@SuppressWarnings("unchecked")
@Override
public List<Animal> listarAnimaisEvt(int id) {
	try {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select a from Evento e join e.animais a where e.id = (select e.id from Evento e where e.id = :id)");
		query.setParameter("id", id);
		return (List<Animal>) query.getResultList();
	} catch (Exception e) {
		return null;
	}
}
}
