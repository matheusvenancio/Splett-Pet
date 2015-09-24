package video.dao;

import java.util.List;

import video.Video;
import dao.Dao;

public interface VideoDao extends Dao<Video> {
	public Video findByUrl(String url);
	public List<Video> findVideosByPubli(Integer id);
}
