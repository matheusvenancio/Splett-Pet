package video.mb;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import video.Video;

public class VideoDataModel extends ListDataModel<Video> implements
		SelectableDataModel<Video> {


	public VideoDataModel() {
	}

	public VideoDataModel(List<Video> data) {
		super(data);
	}

	@Override
	public Video getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<Video> vds = (List<Video>) getWrappedData();

		for (Video vd : vds) {
			if (vd.getUrl().equals(rowKey))
				return vd;
		}

		return null;
	}

	@Override
	public Object getRowKey(Video vd) {
		return vd.getUrl();
	}


}