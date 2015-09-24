package usuario;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ManagedBean(name = "sessionMB")
@SessionScoped
public class SessionMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	public SessionMB() {
		try {
			usuario = new Usuario();

			SecurityContext context = SecurityContextHolder.getContext();
			if (context instanceof SecurityContext) {
				Authentication authentication = context.getAuthentication();
				if (authentication instanceof Authentication) {
					usuario.setUsername(((User) authentication.getPrincipal())
							.getUsername());
				}
			}
		} catch (Exception e) {
			this.negado();
		}

	}

	private String negado() {
		return "negado";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}