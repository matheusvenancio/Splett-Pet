package splett.postagem.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import splett.dao.GenericDao;
import splett.postagem.Postagem;

@ManagedBean(name = "postagemDao")
@ApplicationScoped
public class PostagemDaoImpl extends GenericDao<Postagem>implements PostagemDao {

    private static final long serialVersionUID = 1L;

    public PostagemDaoImpl() {
	super(Postagem.class);
    }

    @SuppressWarnings("unchecked")
    public List<Postagem> listarPostagens(Integer id) {
	EntityManager em = emf.createEntityManager();
	Query query = em.createQuery(
		"SELECT p from Postagem p where usuario_id IN :id_usuario order by p.id desc");
	query.setParameter("id_usuario", id);
	return query.getResultList();
    }
}
