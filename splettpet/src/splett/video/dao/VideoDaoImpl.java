package splett.video.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import splett.dao.GenericDao;
import splett.video.Video;

@ManagedBean(name = "videoDao")
@ApplicationScoped
public class VideoDaoImpl extends GenericDao<Video> implements VideoDao {

	private static final long serialVersionUID = 1L;

	public VideoDaoImpl() {
		super(Video.class);
	}
}
