package splett.postagem.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import splett.dao.GenericDao;
import splett.postagem.Postagem;

@ManagedBean(name = "postagemDao")
@ApplicationScoped
public class PostagemDaoImpl extends GenericDao<Postagem> implements PostagemDao {

	private static final long serialVersionUID = 1L;

	public PostagemDaoImpl() {
		super(Postagem.class);
	}
}
