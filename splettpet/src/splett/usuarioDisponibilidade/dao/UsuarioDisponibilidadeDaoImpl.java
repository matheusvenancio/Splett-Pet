package splett.usuarioDisponibilidade.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import splett.dao.GenericDao;
import splett.usuario.Usuario;
import splett.usuarioDisponibilidade.UsuarioDisponibilidade;

@ManagedBean(name = "usuarioDisponibilidadeDao")
@ApplicationScoped
public class UsuarioDisponibilidadeDaoImpl extends GenericDao<UsuarioDisponibilidade>
	implements UsuarioDisponibilidadeDao {

    private static final long serialVersionUID = 1L;

    public UsuarioDisponibilidadeDaoImpl() {
	super(UsuarioDisponibilidade.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UsuarioDisponibilidade> list(Usuario usuario) {
	EntityManager em = emf.createEntityManager();
	Query q = em.createQuery("select ud from UsuarioDisponibilidade ud where ud.usuario.id = :id");
	q.setParameter("id", usuario.getId());
	return q.getResultList();
    }
}
