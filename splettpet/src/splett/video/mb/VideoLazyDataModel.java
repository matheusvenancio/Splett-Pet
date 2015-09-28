package splett.video.mb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import splett.video.Video;
import splett.video.dao.VideoDao;

@ManagedBean(name = "videoLazyDataModel")
@ViewScoped
public class VideoLazyDataModel extends LazyDataModel<Video>{
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{videoDao}")
	private VideoDao videoDao;

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

			source = videoDao.list(first, pageSize);

		// sort
		if (sortField != null) {
			Collections.sort(source,
					new LazyVideoSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(videoDao.getRowCount());

		return source;
	}
}
