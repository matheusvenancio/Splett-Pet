package splett.amizade.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import splett.amizade.Amizade;
import splett.dao.GenericDao;
import splett.usuario.Usuario;

@ManagedBean(name = "amizadeDao")
@ApplicationScoped
public class AmizadeDaoImpl extends GenericDao<Amizade>implements AmizadeDao {

    private static final long serialVersionUID = 1L;

    public AmizadeDaoImpl() {
	super(Amizade.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Usuario> listAmigos(Usuario usuario) {
	EntityManager em = emf.createEntityManager();
	Query q = em.createQuery(
		"Select u from Usuario u, Amizade a where a.usuarioOrigem.id = :id or a.usuarioDestino.id = :id and a.status = 'ACEITO'");
	q.setParameter("id", usuario.getId());
	return q.getResultList();
    }
}
