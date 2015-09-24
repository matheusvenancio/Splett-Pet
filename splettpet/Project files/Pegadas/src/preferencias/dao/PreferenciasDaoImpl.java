package preferencias.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import preferencias.Preferencias;
import usuario.Usuario;
import dao.GenericDao;

@ManagedBean(name = "preferenciasDao")
@ApplicationScoped
public class PreferenciasDaoImpl extends GenericDao<Preferencias> implements
		PreferenciasDao {

	private static final long serialVersionUID = 1L;

	public PreferenciasDaoImpl() {
		super(Preferencias.class);
	}

	public Usuario getUsuarioByCod(String cod) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select u from Usuario u where u.preferencias.codValidaEmail = :cod");
		query.setParameter("cod", cod);
		return (Usuario) query.getSingleResult();
	}
}
