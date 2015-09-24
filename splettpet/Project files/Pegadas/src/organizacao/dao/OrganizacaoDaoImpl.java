package organizacao.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import banco.Banco;

import necessidade.Necessidade;

import organizacao.Organizacao;

import animal.Animal;
import dao.GenericDao;
import evento.Evento;

@ManagedBean(name = "organizacaoDao")
@ApplicationScoped
public class OrganizacaoDaoImpl extends GenericDao<Organizacao> implements
		OrganizacaoDao {
	private static final long serialVersionUID = 1L;

	public OrganizacaoDaoImpl() {
		super(Organizacao.class);
	}

	@SuppressWarnings("unchecked")
	public List<Organizacao> findOrgsPendentes() {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select o from Organizacao o where o.isConfirmado = false order by o.dataCadastro desc");
		return (List<Organizacao>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Organizacao> findOrgs() {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select o from Organizacao o where o.isConfirmado = true order by o.dataCadastro asc");
		return (List<Organizacao>) query.getResultList();
	}

	public Organizacao findOrgByMod(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select o from Organizacao o where o.moderador.id = :id");
		query.setParameter("id", id);
		return (Organizacao) query.getSingleResult();
	}

	public Organizacao findOrgByVol(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select o from Organizacao o join o.voluntarios v where v.id = :id");
		query.setParameter("id", id);
		return (Organizacao) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Evento> listEventoByOrg(int id) {
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select e from Evento e where e.organizacao = (select o from Organizacao o where o.id = :id)");
			query.setParameter("id", id);
			return (List<Evento>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Necessidade> listNecessidadesByOrg(int id) {
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select n from Necessidade n where n.organizacao = (select o from Organizacao o where o.id = :id)");
			query.setParameter("id", id);
			return (List<Necessidade>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Organizacao findOrgByNecessidade(Necessidade necessidade) {
		int id = necessidade.getOrganizacao().getId();
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select o from Organizacao o where o.id = :id");
			query.setParameter("id", id);
			return (Organizacao) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Animal> listAnimaisByOrg(int id) {
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select a from Organizacao o join o.animais a where o.id = (select o from Organizacao o where o.id = :id)");
			query.setParameter("id", id);
			return (List<Animal>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Evento ultimoEventoTermino(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select max(e.dataCadastro) from Organizacao o join o.eventos e where o.id = :id");
		query.setParameter("id", id);
		return (Evento) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Evento> listUltimosEventoByOrg(int id) {
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select e from Evento e where e.organizacao = (select o from Organizacao o where o.id = :id) order by e.dataCadastro desc");
			query.setParameter("id", id);
			return (List<Evento>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public Organizacao pesquisarOrgByNome(String nome) {
		System.out.println("dao - nome: " + nome);
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select o from Organizacao o where lower(o.nome) like concat('%', :nome, '%')");
		query.setParameter("nome", nome);

		return (Organizacao) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Organizacao> pesquisarOrgByMod(String nomeMod) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select o from Organizacao o where lower(o.moderador.nome) like concat('%', :nome, '%')");
		query.setParameter("nome", nomeMod);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Organizacao> pesquisarOrganizacoes(String nome,
			Date dataCadastro, String nomeMod, String estado) {
		EntityManager em = emf.createEntityManager();
		StringBuffer query = new StringBuffer();
		query.append("select o from Organizacao o join o.enderecos e where ");
		List<String> criteria = new ArrayList<String>();

		try {
			if (nome != null) {
				criteria.add("(o.nome like concat('%', :nome, '%'))");
			}
			if (dataCadastro != null) {
				criteria.add("o.dataCadastro = :dataCadastro");
			}
			if (estado != null) {
				criteria.add("e.estado.nome = :estado");
			}
			if (nomeMod != null) {
				criteria.add("o.moderador.nome like concat('%', :nomeMod, '%')");
			}
			criteria.add("o.isConfirmado = true");
			for (int i = 0; i < criteria.size(); i++) {
				if (i > 0) {
					query.append(" and ");
				}
				query.append(criteria.get(i));
			}
			System.out.println("QUERY ORG 1: " + query.toString());
			Query q = em.createQuery(query.toString());
			if (nome != null) {
				System.out.println("PARAMETRO nome: " + nome);
				q.setParameter("nome", nome);
			}
			if (dataCadastro != null) {
				q.setParameter("dataCadastro", dataCadastro);
			}
			if (estado != null) {
				q.setParameter("estado", estado);
			}
			if (nomeMod != null) {
				q.setParameter("nomeMod", nomeMod);
			}
			System.out.println("QUERY ORG: " + query.toString());
			return (List<Organizacao>) q.getResultList();
		} catch (Exception e) {
			System.out.println("catch");
			e.printStackTrace();
			return null;

		}
	}

	@Override
	public Organizacao buscarOrgUsuario(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select o from Organizacao o where o.moderador.id = (select u.id from Usuario u where u.id = :id)");
		query.setParameter("id", id);

		return (Organizacao) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Organizacao> listarOrgsGerenciar(String estado,
			boolean filtrarOrgsBanidas) {
		EntityManager em = emf.createEntityManager();
		StringBuffer query = new StringBuffer();
		query.append("select o from Organizacao o join o.enderecos e where ");
		List<String> criteria = new ArrayList<String>();

		try {
			if (estado != null) {
				criteria.add("e.estado.nome = :estado");
			}
			criteria.add("o.isConfirmado = :filtrarOrgsBanidas");
			for (int i = 0; i < criteria.size(); i++) {
				if (i > 0) {
					query.append(" and ");
				}
				query.append(criteria.get(i));
			}
			System.out.println("QUERY ORG 1: " + query.toString());
			Query q = em.createQuery(query.toString());
			if (estado != null) {
				q.setParameter("estado", estado);
			}
			q.setParameter("filtrarOrgsBanidas", filtrarOrgsBanidas);

			return (List<Organizacao>) q.getResultList();
		} catch (Exception e) {
			System.out.println("catch");
			e.printStackTrace();
			return null;

		}
	}

	@SuppressWarnings("unchecked")
	public List<Organizacao> listarOrgsGerenciarPendentes(boolean filtrarOrgsP) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select o from Organizacao o where o.isConfirmado = :filtrarOrgsP order by o.dataCadastro desc");
		query.setParameter("filtrarOrgsP", filtrarOrgsP);
		return (List<Organizacao>) query.getResultList();
	}
	
	
	@Override
	public Banco verificarExisteBB(int id) {
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em
					.createQuery("select b from Banco b where b.tipoBanco = 'BANCOBRASIL' and b.organizacao.id = (select o.id from Organizacao o where o.id = :id)");
			query.setParameter("id", id);

			return (Banco) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Banco verificarExisteBradesco(int id) {
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em
					.createQuery("select b from Banco b where b.tipoBanco = 'BRADESCO' and b.organizacao.id = (select o.id from Organizacao o where o.id = :id)");
			query.setParameter("id", id);

			return (Banco) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Banco verificarExisteItau(int id) {
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em
					.createQuery("select b from Banco b where b.tipoBanco = 'ITAU' and b.organizacao.id = (select o.id from Organizacao o where o.id = :id)");
			query.setParameter("id", id);

			return (Banco) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	

	
}
