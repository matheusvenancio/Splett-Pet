package splett.usuario.endereco.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import splett.dao.GenericDao;
import splett.endereco.Logradouro;
import splett.usuario.endereco.Endereco;

@ManagedBean(name="enderecoDao")
@ApplicationScoped
public class EnderecoDaoImpl extends GenericDao<Endereco> implements EnderecoDao{
	
	private static final long serialVersionUID= 1L;
	private EntityManager em;
	
	public EnderecoDaoImpl()
	{
		super(Endereco.class);
	}
	

	@SuppressWarnings("unchecked")
	public List<Logradouro> pesquisarPorCep(String cep){
		em = emf.createEntityManager();
		Query q = this.em.createNamedQuery("Logradouro.pesquisaPorCep");
		q.setParameter("cep", cep);
		q.setMaxResults(50);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Endereco> pesquisarPorCidade(String nome){
		EntityManager em = emf.createEntityManager();
		Query q = em
				.createQuery("select e from Endereco e where lower(e.cidade.nome) like concat('%', :nome, '%')");
		q.setParameter("nome", nome);
		q.setMaxResults(50);
		return q.getResultList();
	}
	
}
