package splett.video.mb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import splett.session.SessionMB;
import splett.usuario.Usuario;
import splett.video.Video;
import splett.video.dao.VideoDao;

@ManagedBean(name = "videoLazyDataModel")
@ViewScoped
public class VideoLazyDataModel extends LazyDataModel<Video> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{videoDao}")
	private VideoDao videoDao;
	
	@ManagedProperty(value = "#{sessionMB}")
	private SessionMB sessionMB;

	@Override
	public Video getRowData(String rowKey) {
		return videoDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Video video) {
		return video.getId();
	}

	@Override
	public List<Video> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Video> source = null;
		Usuario u = new Usuario();
		u = sessionMB.getUsuarioLogado();

		source = videoDao.listVideo(u.getId());

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyVideoSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(videoDao.getRowCount());

		return source;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public VideoDao getVideoDao() {
		return videoDao;
	}

	public void setVideoDao(VideoDao videoDao) {
		this.videoDao = videoDao;
	}

	public SessionMB getSessionMB() {
		return sessionMB;
	}

	public void setSessionMB(SessionMB sessionMB) {
		this.sessionMB = sessionMB;
	}
	

}
