package video.dao;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import video.Video;
import dao.GenericDao;

@ManagedBean(name = "videoDao")
@ApplicationScoped
public class VideoDaoImpl extends GenericDao<Video> implements VideoDao {
	private static final long serialVersionUID = 1L;
	
	public VideoDaoImpl() {
		super(Video.class);
	}
	
	public Video findByUrl(String url) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select v from Video v where v.url = :url");
		query.setParameter("url", url);
		return (Video) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Video> findVideosByPubli(Integer id) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select v from Video v where v.animal.id = (select a.id from Animal a where a.id = :id)");
		query.setParameter("id", id);
		return (List<Video>) query.getResultList();
	}

}
