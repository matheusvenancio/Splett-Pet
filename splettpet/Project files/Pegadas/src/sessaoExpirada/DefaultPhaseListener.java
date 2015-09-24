package sessaoExpirada;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import usuario.fb.FacesUtils;
  
public class DefaultPhaseListener implements PhaseListener {  
  
    private static final long serialVersionUID = -1065005858605121693L;  
   // private static final String viewSessaoExpirada = "/faces/views/comum/erroSessaoExpirada.xhtml";  
  
    @Override  
    public void afterPhase(PhaseEvent event) throws ViewExpiredException{  
        FacesContext context = event.getFacesContext();  
        ExternalContext ext = context.getExternalContext();  
        HttpSession session = (HttpSession) ext.getSession(false);  
        boolean newSession = (session == null) || (session.isNew());  
        boolean timedout = newSession;  
        if (timedout) {  
            Application app = context.getApplication();  
            ViewHandler viewHandler = app.getViewHandler();  
            UIViewRoot view = viewHandler.createView(context, "index");  
            context.setViewRoot(view);  
            context.renderResponse(); 
            try {
				FacesUtils.getExternalContext().redirect("/Pegadas/sessaoExpirada.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
            try {  
                viewHandler.renderView(context, view);  
                context.responseComplete();  
            } catch (Throwable t) {  
                throw new FacesException("Session timed out", t);  
            }  
        }  
    }    
  
    @Override  
    public void beforePhase(PhaseEvent event) {  
        //OLD  
    }  
  
    @Override  
    public PhaseId getPhaseId() {  
        return PhaseId.RESTORE_VIEW;  
    }  
}  