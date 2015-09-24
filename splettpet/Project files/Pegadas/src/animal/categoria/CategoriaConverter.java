package animal.categoria;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import animal.categoria.dao.CategoriaDao;

@ManagedBean(name = "categoriaConverter")
@ApplicationScoped
public class CategoriaConverter implements Converter {

	@ManagedProperty(value = "#{categoriaDao}")
	private CategoriaDao categoriaDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		if (value != null && !value.isEmpty()
				&& !value.equalsIgnoreCase("Outro"))
			return categoriaDao.findById(Integer.parseInt(value));
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) throws ConverterException {
		if (object instanceof Categoria)
			return ((Categoria)object).getId().toString();
		return null;
	}

	public CategoriaDao getCategoriaDao() {
		return categoriaDao;
	}

	public void setCategoriaDao(CategoriaDao categoriaDao) {
		this.categoriaDao = categoriaDao;
	}

}