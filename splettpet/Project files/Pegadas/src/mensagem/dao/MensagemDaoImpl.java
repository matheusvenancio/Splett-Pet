package mensagem.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import mensagem.Mensagem;
import usuario.Usuario;
import dao.GenericDao;

@ManagedBean(name = "mensagemDao")
@ApplicationScoped
public class MensagemDaoImpl extends GenericDao<Mensagem> implements
		MensagemDao {
	private static final long serialVersionUID = 1L;

	public MensagemDaoImpl() {
		super(Mensagem.class);
	}

	@SuppressWarnings("unchecked")
	public List<Mensagem> getCaixaEntrada(Usuario destinatario) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select m from Mensagem m where m.destinatario.id = :id");
		q.setParameter("id", destinatario.getId());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Mensagem> getCaixaSaida(Usuario remetente) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select m from Mensagem m where m.remetente.id = :id");
		q.setParameter("id", remetente.getId());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Mensagem> getMsgsNovas(Usuario destinatario) {
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select m from Mensagem m where m.destinatario = :destinatario and m.isLida = false");
		q.setParameter("destinatario", destinatario);
		return q.getResultList();
	}

}
