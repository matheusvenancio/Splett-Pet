package endereco.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import dao.GenericDao;
import endereco.Endereco;

@ManagedBean(name = "enderecoDao")
@ApplicationScoped
public class EnderecoDaoImpl extends GenericDao<Endereco> implements
		EnderecoDao {

	private static final long serialVersionUID = 1L;

	public EnderecoDaoImpl() {
		super(Endereco.class);
	}

	@SuppressWarnings("unchecked")
	public List<Endereco> findEnderecoByOrg(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select e from Endereco e where e.idEndereco = (select oe.id from Organizacao o join o.enderecos oe where oe.id = :id)");
		query.setParameter("id", id);
		return (List<Endereco>) query.getResultList();
	}

}
