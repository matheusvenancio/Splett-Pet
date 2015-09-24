package layout;

import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext;  
import org.primefaces.component.layout.LayoutUnit;  
  
import org.primefaces.event.CloseEvent;  
import org.primefaces.event.ToggleEvent;  
  
public class LayoutBean {  
  
    public void handleClose(CloseEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit Closed", "Position:'" + ((LayoutUnit)event.getComponent()).getPosition() + "'");  
  
        addMessage(message);  
    }  
  
    public void handleToggle(ToggleEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, ((LayoutUnit)event.getComponent()).getPosition() + " toggled", "Status:" + event.getVisibility().name());  
  
        addMessage(message);  
    }  
      
    private void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
}  