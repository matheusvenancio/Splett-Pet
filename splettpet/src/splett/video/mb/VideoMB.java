package splett.video.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.session.SessionMB;
import splett.usuario.Usuario;
import splett.video.Video;
import splett.video.dao.VideoDao;

@ManagedBean(name = "videoMB")
@ViewScoped
public class VideoMB {

	@ManagedProperty(value = "#{videoDao}")
	private VideoDao videoDao;

	@ManagedProperty(value = "#{videoLazyDataModel}")
	private VideoLazyDataModel videoLazyDataModel;

	@ManagedProperty(value = "#{sessionMB}")
	private SessionMB sessionMB;

	private List<Video> videosFiltered;

	private Video video;

	public VideoMB() {
		videosFiltered = new ArrayList<Video>();
	}

	public void criar() {
		video = new Video();
	}

	public void salvar() {
		if (video.getId() != null) {
			videoDao.update(video);
		} else {
			video.setUsuario(new Usuario());
			video.setUsuario(sessionMB.getUsuarioLogado());
			videoDao.salvar(video);
		}
	}

	public void remover() {
		videoDao.remover(video);
	}

	public void cancelar() {
		video = null;
	}

	public VideoDao getVideoDao() {
		return videoDao;
	}

	public void setVideoDao(VideoDao videoDao) {
		this.videoDao = videoDao;
	}

	public VideoLazyDataModel getVideoLazyDataModel() {
		return videoLazyDataModel;
	}

	public void setVideoLazyDataModel(VideoLazyDataModel videoLazyDataModel) {
		this.videoLazyDataModel = videoLazyDataModel;
	}

	public List<Video> getVideosFiltered() {
		return videosFiltered;
	}

	public void setVideosFiltered(List<Video> videosFiltered) {
		this.videosFiltered = videosFiltered;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public SessionMB getSessionMB() {
		return sessionMB;
	}

	public void setSessionMB(SessionMB sessionMB) {
		this.sessionMB = sessionMB;
	}
	

}
