package usuario.fb;

//~--- JDK imports ------------------------------------------------------------
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FacesUtils {

	public static void addErrorMessageExcecao(Exception ex, String defaultMsg) {
		String msg = ex.getLocalizedMessage();

		if ((msg != null) && (msg.length() > 0)) {
			addErrorMessage(msg);
		} else {
			addErrorMessage(defaultMsg);
		}
	}

	public static void addErrorMessages(List<String> messages) {
		for (String message : messages) {
			addErrorMessage(message);
		}
	}

	public static void addErrorMessageComponent(String idComponente, String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				msg, msg);
		FacesContext.getCurrentInstance().addMessage(idComponente, facesMsg);
	}

	public static void addErrorMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	public static void addSuccessMessageComponent(String idComponente,
			String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				msg, msg);
		FacesContext.getCurrentInstance().addMessage("idComponente", facesMsg);
	}

	public static void addSuccessMessage(String msg) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				msg, msg);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	public static String getRequestParameter(final String param) {
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		return context.getRequestParameterMap().get(param);
	}

	public static Object getObjectFromRequestParameter(
			String requestParameterName, Converter converter,
			UIComponent component) {
		String theId = FacesUtils.getRequestParameter(requestParameterName);
		return converter.getAsObject(FacesContext.getCurrentInstance(),
				component, theId);
	}

	public static Object getMethod(Object obj, String name) throws Exception {
		Method createMethod = obj.getClass().getMethod(name, new Class[0]);
		return createMethod.invoke(obj, new Object[0]);
	}

	public static void setAttribute(String chave, Object valor) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.put(chave, valor);
	}

	public static Object getAttribute(Object classe, String valorObjeto) {
		return FacesContext
				.getCurrentInstance()
				.getApplication()
				.getELResolver()
				.getValue(FacesContext.getCurrentInstance().getELContext(),
						null, valorObjeto);
	}

	public static Map<String, Object> getSessionMap() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap();
	}

	public static ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
	}

	public static HttpServletRequest getServletRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	//

	public static HttpServletResponse getServletResponse() {
       try{
	
    	   return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    	   
       }
       catch (Exception e) {
		System.out.println("voltou nulo");
    	   return null;
		
	}
	}
	//

	public static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}

	public static String getApplicationURI() {
		try {
			URI uri = new URI(getExternalContext().getRequestScheme(), null,
					getExternalContext().getRequestServerName(),
					getExternalContext().getRequestServerPort(),
					getExternalContext().getRequestContextPath()
							+ getExternalContext().getRequestServletPath(),
					null, null);
			return uri.toASCIIString();
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * Finds component with the given id
	 */
	public static UIComponent findComponent(UIComponent c, String id) {
		if (id.equals(c.getId())) {
			return c;
		}
		Iterator<UIComponent> kids = c.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}
}
