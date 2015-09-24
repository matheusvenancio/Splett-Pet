package sistema;

import javax.faces.component.UIComponent;  
import javax.faces.context.FacesContext;  
import javax.faces.convert.Converter;  
import javax.faces.convert.FacesConverter;  

@FacesConverter(value="ConverterEmptyToNull")  
public class ConverterEmptyToNull implements Converter{  
  
    @Override  
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {  
          
        if( value.equalsIgnoreCase("") ){  
            value=null;  
        }  
        return value;  
    }  
  
    @Override  
    public String getAsString(FacesContext facesContext, UIComponent component, Object obj) {  
          
        return obj.toString();  
          
    }  
}
