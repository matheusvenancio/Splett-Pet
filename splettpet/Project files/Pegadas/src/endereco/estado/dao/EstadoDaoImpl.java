package endereco.estado.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import dao.GenericDao;
import endereco.estado.Estado;

@ManagedBean(name="estadoDao")
@ApplicationScoped
public class EstadoDaoImpl extends GenericDao<Estado> implements EstadoDao{
	
	private static final long serialVersionUID = 1L;

	public EstadoDaoImpl() {
		super(Estado.class);
	}
}
