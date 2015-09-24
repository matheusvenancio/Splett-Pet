package endereco.estado;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import endereco.estado.dao.EstadoDao;

@ManagedBean(name = "estadoConverter")
@ApplicationScoped
public class EstadoConverter implements Converter {

	@ManagedProperty(value = "#{estadoDao}")
	private EstadoDao estadoDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		if (value != null && !value.isEmpty()
				&& !value.equalsIgnoreCase("Selecione"))
			return estadoDao.findById(Integer.parseInt(value));
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) throws ConverterException {
		if (object instanceof Estado)
			return ((Estado) object).getId().toString();
		return null;
	}

	public EstadoDao getEstadoDao() {
		return estadoDao;
	}

	public void setEstadoDao(EstadoDao estadoDao) {
		this.estadoDao = estadoDao;
	}

}
