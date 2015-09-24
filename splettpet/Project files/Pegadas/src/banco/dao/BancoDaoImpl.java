package banco.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import organizacao.Organizacao;

import banco.Banco;
import banco.tipo.TipoBanco;
import dao.GenericDao;
import doacao.banco.BancoDoacao;

@ManagedBean(name = "bancoDao")
@ApplicationScoped
public class BancoDaoImpl extends GenericDao<Banco> implements BancoDao {
	private static final long serialVersionUID = 1L;

	public BancoDaoImpl() {
		super(Banco.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Banco> listarBancosOrg(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select b from Banco b where b.organizacao.id = :id");
		query.setParameter(id, "id");
		return (List<Banco>) query.getResultList();
	}
	
	@Override
	public Banco findBancoBoleto(int id, BancoDoacao bancoDoacao) {
		
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select b from Banco b where b.organizacao.id = :id and b.tipoBanco = 'BANCOBRASIL'");
		query.setParameter("id", id);
		return (Banco) query.getSingleResult();
	}
	
	@Override
	public Banco findBancoBoleto2(int id, BancoDoacao bancoDoacao) {
		
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select b from Banco b where b.organizacao.id = :id and b.tipoBanco = 'BRADESCO'");
		query.setParameter("id", id);
		return (Banco) query.getSingleResult();
	}
	
	
	@Override
	public Banco findBancoBoleto3(int id, BancoDoacao bancoDoacao) {
		
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select b from Banco b where b.organizacao.id = :id and b.tipoBanco = 'ITAU'");
		query.setParameter("id", id);
		return (Banco) query.getSingleResult();
	}
	
	
}
