package arquivo.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import arquivo.Arquivo;
import dao.GenericDao;

@ManagedBean(name = "arquivoDao")
@ApplicationScoped
public class ArquivoDaoImpl extends GenericDao<Arquivo> implements ArquivoDao {
	private static final long serialVersionUID = 1L;

	public ArquivoDaoImpl() {
		super(Arquivo.class);
	}

	public Arquivo findAvatarPadrao() {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select a from Arquivo a where a.id = 1");
		return (Arquivo) query.getSingleResult();
	}

	public Arquivo findByNome(String nome) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select a from Arquivo a where a.nome = :nome");
		query.setParameter("nome", nome);
		return (Arquivo) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Arquivo> findArquivosByPubli(Integer id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select a from Arquivo a where a.id = (select f.id from Publicacao p join p.arquivos f where f.id = :id)");
		query.setParameter("id", id);
		return (List<Arquivo>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Arquivo> findArquivosByMsg(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select a from Mensagem m join m.arquivos a where m.id = :id");
		query.setParameter("id", id);
		return (List<Arquivo>) query.getResultList();
	}
	
	//				.createQuery("select a from Arquivo a join tbMensagem_tbArquivo f where a.id = f.a.id and f.tbMensagem_id = :id");
}
