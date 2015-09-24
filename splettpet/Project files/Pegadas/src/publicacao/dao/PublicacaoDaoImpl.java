package publicacao.dao;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;

import publicacao.Publicacao;
import publicacao.tipoPublicacao.TipoPublicacao;
import animal.categoria.Categoria;
import animal.divisaoIdade.DivisaoIdade;
import animal.porte.Porte;
import animal.raca.Raca;
import animal.sexo.Sexo;
import dao.GenericDao;
import endereco.cidade.Cidade;
import endereco.estado.Estado;

@ManagedBean(name = "publicacaoDao")
@ApplicationScoped
public class PublicacaoDaoImpl extends GenericDao<Publicacao> implements
		PublicacaoDao {
	private static final long serialVersionUID = 1L;

	public PublicacaoDaoImpl() {
		super(Publicacao.class);
	}

	public static final int BUSCA_TITULO = 1;
	public static final int BUSCA_TIPO = 2;
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Publicacao> listarPublicacoes() {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select p from Publicacao p where p.tipoPublicacao is not null");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Publicacao> pesquisarPubliByTitulo(String titulo) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select p from Publicacao p where lower(p.titulo) like concat('%', :titulo, '%')");
		query.setParameter("titulo", titulo);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Publicacao> findPubliByOrg(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select p from Publicacao p where p.usuario.id = (select v.id from Organizacao o join o.voluntarios v where v.id = :id or o.moderador.id =  :id)");
		query.setParameter("id", id);

		return (List<Publicacao>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Publicacao> pesquisarT(String tf, TipoPublicacao tipoPubli,
			Categoria categoria, Estado estado) {
		EntityManager em = emf.createEntityManager();
		StringBuffer query = new StringBuffer();
		query.append("select p from Publicacao p join p.animais a where ");
		List<String> criteria = new ArrayList<String>();
		if (tf != null) {
			criteria.add("(p.titulo like concat('%', :titulo, '%') or a.nome like concat('%', :nome, '%'))");
		}
		if (tipoPubli != null) {
			criteria.add("p.tipoPublicacao = :tipoPublicacao");
		}
		if (categoria != null) {
			criteria.add("a.categoria = :categoria");
		}
		if (estado != null) {
			criteria.add("p.endereco.estado = :estado");
		}
		for (int i = 0; i < criteria.size(); i++) {
			if (i > 0) {
				query.append(" and ");
			}
			query.append(criteria.get(i));
		}
		Query q = em.createQuery(query.toString());
		if (tf != null) {
			q.setParameter("titulo", tf);
			q.setParameter("nome", tf);
		}
		if (tipoPubli != null) {
			q.setParameter("tipoPublicacao", tipoPubli);
		}
		if (categoria != null) {
			q.setParameter("categoria", categoria);
		}
		if (estado != null) {
			q.setParameter("estado", estado);
		}
		System.out.println("QUERY PLB: " + query.toString());
		return (List<Publicacao>) q.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Publicacao> pesquisar(String tf, TipoPublicacao tipoPubli,
			Categoria categoria, Raca raca, Sexo sexo, Porte porte,
			String corPrincipal, String corSecundaria, DivisaoIdade diviIdade,
			Estado estado) {
		
	//	System.out.println("tf: " + tf + "tipoPublicacao: " + tipoPubli.toString() + "categoria: " + categoria.toString() + "raca: " + raca);
		//System.out.println("sexo: " + sexo.toString() + "porte: " + porte.toString());
		//System.out.println("corprincipal: " + corPrincipal);
		//System.out.println("corsecundaria: " + corSecundaria);
	//	System.out.println("div idade: " + diviIdade);
	//	System.out.println("estado: " + estado.getNome());
		EntityManager em = emf.createEntityManager();
		StringBuffer query = new StringBuffer();
		query.append("select p from Publicacao p join p.animais a join p.endereco e where ");
		List<String> criteria = new ArrayList<String>();
		if (tf != null) {
			criteria.add("(a.nome like concat('%', :nome, '%'))");
		}
		if (tipoPubli != null) {
			criteria.add("(p.tipoPublicacao = :tipoPublicacao)");
		}
		if (categoria != null) {
			criteria.add("(a.categoria = :categoria)");
		}
		if (raca != null) {
			criteria.add("(a.raca = :raca)");
		}
		if (sexo != null) {
			criteria.add("(a.sexo = :sexo)");
		}
		if (porte != null) {
			criteria.add("(a.porte = :porte)");
		}
		if (diviIdade != null) {
			criteria.add("(a.divisaoIdade = :divisaoIdade)");
		}
		if (corPrincipal != null) {
			criteria.add("(a.corPrincipal like concat ('%', :corPrincipal, '%'))");
		}
		if (corSecundaria != null) {
			criteria.add("(a.corSecundaria like concat ('%', :corSecundaria, '%'))");
		}
		if (estado != null) {
			criteria.add("e.estado = :estado");
		}
		for (int i = 0; i < criteria.size(); i++) {
			if (i > 0) {
				query.append(" and ");
			}
			query.append(criteria.get(i));
		}
		Query q = em.createQuery(query.toString());
		if (tf != null) {
			q.setParameter("nome", tf);
		}
		if (tipoPubli != null) {
			q.setParameter("tipoPublicacao", tipoPubli);
		}
		if (categoria != null) {
			q.setParameter("categoria", categoria);
		}
		if (raca != null) {
			q.setParameter("raca", raca);
		}
		if (sexo != null) {
			q.setParameter("sexo", sexo);
		}
		if (porte != null) {
			q.setParameter("porte", porte);
		}
		if (diviIdade != null) {
			q.setParameter("divisaoIdade", diviIdade);
		}
		if (corPrincipal != null) {
			q.setParameter("corPrincipal", corPrincipal);
		}
		if (corSecundaria != null) {
			q.setParameter("corSecundaria", corSecundaria);
		}
		if (estado != null) {
			q.setParameter("estado", estado);
		}
		System.out.println("QUERY PLB: " + query.toString());
		return (List<Publicacao>) q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Publicacao> match(TipoPublicacao tipoPubli,
			Categoria categoria, Raca raca, Sexo sexo, String cor,
			DivisaoIdade diviIdade, Estado estado, Cidade cidade) {
		EntityManager em = emf.createEntityManager();
		StringBuffer query = new StringBuffer();
		query.append("select p from Publicacao p join p.animais a join p.endereco e where ");
		List<String> criteria = new ArrayList<String>();

		if (tipoPubli != null) {
			criteria.add("(p.tipoPublicacao = :tipoPublicacao)");
		}
		if (categoria != null) {
			criteria.add("(a.categoria = :categoria)");
		}
		if (raca != null) {
			criteria.add("(a.raca = :raca)");
		}
		if (sexo != null && sexo != Sexo.NA) {
			criteria.add("(a.sexo = :sexo)");
		}
		if (diviIdade != null) {
			criteria.add("a.divisaoIdade = :divisaoIdade");
		}
		if (cor != null) {
			criteria.add("(a.corPrincipal = :corPrincipal or a.corSecundaria = :corSecundaria)");
		}
		if (estado != null) {
			criteria.add("e.estado = :estado");
		}
		if (cidade != null) {
			criteria.add("e.cidade = :cidade");
		}
		for (int i = 0; i < criteria.size(); i++) {
			if (i > 0) {
				query.append(" and ");
			}
			query.append(criteria.get(i));
		}
		Query q = em.createQuery(query.toString());
		if (tipoPubli == TipoPublicacao.ENCONTRADO) {
			q.setParameter("tipoPublicacao", TipoPublicacao.PERDIDO);
		}
		if (tipoPubli == TipoPublicacao.PERDIDO) {
			q.setParameter("tipoPublicacao", TipoPublicacao.ENCONTRADO);
		}
		if (categoria != null) {
			q.setParameter("categoria", categoria);
		}
		if (raca != null) {
			q.setParameter("raca", raca);
		}
		if (sexo != null && sexo != Sexo.NA) {
			q.setParameter("sexo", sexo);
		}
		if (diviIdade != null) {
			q.setParameter("divisaoIdade", diviIdade);
		}
		if (cor != null) {
			q.setParameter("corPrincipal", cor);
			q.setParameter("corSecundaria", cor);
		}
		if (estado != null) {
			q.setParameter("estado", estado);
		}
		if (cidade != null) {
			q.setParameter("cidade", cidade);
		}
		System.out.println(query.toString());
		return (List<Publicacao>) q.getResultList();
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	@SuppressWarnings("unchecked")
	public List<Publicacao> publiByUsr(int id) {
		System.out.println("no dao: id: " + id);
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select p from Publicacao p where p.usuario.id = :id and p.tipoPublicacao is not null");
			query.setParameter("id", id);
			return (List<Publicacao>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

}
