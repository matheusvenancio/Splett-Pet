package animal;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean(name = "animalConverter")
@ApplicationScoped
public class AnimalConverter implements Converter {

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return String.valueOf(((Animal) value).getId());
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		Animal animal = new Animal();
		animal.setId(Integer.parseInt(value));
		return animal;
	}
}
