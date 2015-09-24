package gmap.dao;

import gmap.GMap;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import dao.GenericDao;

@ManagedBean(name = "gMapDao")
@ApplicationScoped
public class GMapDaoImpl extends GenericDao<GMap> implements GMapDao {

	private static final long serialVersionUID = 1L;

	public GMapDaoImpl() {
		super(GMap.class);
	}
	
	@Override
	public GMap buscarGmByEnd(int id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select g from GMap g where g.endereco.id = :id");
		query.setParameter("id", id);
		return (GMap) query.getSingleResult();
	}
}
