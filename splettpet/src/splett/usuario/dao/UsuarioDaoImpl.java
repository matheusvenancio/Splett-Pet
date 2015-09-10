package splett.usuario.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import splett.dao.GenericDao;
import splett.usuario.Usuario;

@ManagedBean(name = "usuarioDao")
@ApplicationScoped
public class UsuarioDaoImpl extends GenericDao<Usuario> implements UsuarioDao {

	private static final long serialVersionUID = 1L;

	public UsuarioDaoImpl() {
		super(Usuario.class);

	}

	@SuppressWarnings("unchecked")
	public List<Usuario> pesquisarPorNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from Usuario u where lower(u.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		return q.getResultList();
	}

	public Usuario pesquisarPorEmail(String email) {

		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select u from Usuario u where lower(u.email) = :email");
		q.setParameter("email", email);
		return (Usuario) q.getSingleResult();
	}

}
