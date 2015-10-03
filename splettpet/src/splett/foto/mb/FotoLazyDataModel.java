package splett.foto.mb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import splett.foto.Foto;
import splett.foto.dao.FotoDao;
import splett.session.SessionMB;
import splett.usuario.Usuario;

@ManagedBean(name = "fotoLazyDataModel")
@ViewScoped
public class FotoLazyDataModel extends LazyDataModel<Foto> {

	private static final long serialVersionUID = 1L;


	@ManagedProperty(value = "#{fotoDao}")
	private FotoDao fotoDao;
	
	
	@ManagedProperty(value = "#{sessionMB}")
	private SessionMB sessionMB;


	@Override
	public Foto getRowData(String rowKey) {
		return fotoDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Foto foto) {
		return foto.getId();
	}

	@Override
	public List<Foto> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Foto> source = null;
		
		Usuario u = new Usuario();
		u = sessionMB.getUsuarioLogado();

		source = fotoDao.listFoto(u.getId());

		// sort
		if (sortField != null) {
			Collections.sort(source, new LazyFotoSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(fotoDao.getRowCount());

		return source;
	}

	public FotoDao getFotoDao() {
		return fotoDao;
	}

	public void setFotoDao(FotoDao fotoDao) {
		this.fotoDao = fotoDao;
	}

	public SessionMB getSessionMB() {
		return sessionMB;
	}

	public void setSessionMB(SessionMB sessionMB) {
		this.sessionMB = sessionMB;
	}
	
	
}
