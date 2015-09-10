package splett.foto.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import splett.dao.GenericDao;
import splett.foto.Foto;

@ManagedBean(name = "fotoDao")
@ApplicationScoped
public class FotoDaoImpl extends GenericDao<Foto> implements FotoDao {

	private static final long serialVersionUID = 1L;

	public FotoDaoImpl() {
		super(Foto.class);
	}
	
}
