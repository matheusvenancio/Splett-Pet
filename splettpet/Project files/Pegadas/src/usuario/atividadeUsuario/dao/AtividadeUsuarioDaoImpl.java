package usuario.atividadeUsuario.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import usuario.atividadeUsuario.AtividadeUsuario;
import dao.GenericDao;

@ManagedBean(name = "atividadeUsuarioDao")
@ApplicationScoped
public class AtividadeUsuarioDaoImpl extends GenericDao<AtividadeUsuario> implements AtividadeUsuarioDao{
	private static final long serialVersionUID = 1L;

	public AtividadeUsuarioDaoImpl() {
		super(AtividadeUsuario.class);
	}
	
	@Override
	public AtividadeUsuario findAtividadeUsuarioById(int id) {
		AtividadeUsuario atividadeUsuario;
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select at from AtividadeUsuario at where at.usuario.id = :id");
		query.setParameter("id", id);
		atividadeUsuario = (AtividadeUsuario) query.getSingleResult();
		return atividadeUsuario;
	}
}
