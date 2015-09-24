package endereco.cidade;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import endereco.cidade.dao.CidadeDao;

@ManagedBean(name = "cidadeConverter")
@ApplicationScoped
public class CidadeConverter implements Converter {

	@ManagedProperty(value = "#{cidadeDao}")
	private CidadeDao cidadeDao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		if (value != null && !value.isEmpty()
				&& !value.equalsIgnoreCase("Selecione"))
			return cidadeDao.findById(Integer.parseInt(value));
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) throws ConverterException {
		if (object instanceof Cidade)
			return ((Cidade) object).getId().toString();
		return null;
	}

	public CidadeDao getCidadeDao() {
		return cidadeDao;
	}

	public void setCidadeDao(CidadeDao cidadeDao) {
		this.cidadeDao = cidadeDao;
	}

}
