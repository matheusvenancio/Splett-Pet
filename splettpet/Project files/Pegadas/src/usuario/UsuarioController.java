package usuario;

import usuario.Usuario;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;


public class UsuarioController implements Serializable {

	private static final long serialVersionUID = 1L;
	private Usuario usuario;

	public UsuarioController() {
		try {
			usuario = new Usuario();
			SecurityContext context = SecurityContextHolder.getContext();
			if (context instanceof SecurityContext) {
				Authentication authentication = context.getAuthentication();
				if (authentication instanceof Authentication) {
					try {
						
						//usuario.setUsername(context.getAuthentication().getPrincipal().toString());
						usuario.setUsername(((User) authentication.getPrincipal())
								.getUsername());
					} catch (Exception e) {
						this.usuario = (Usuario) context.getAuthentication().getPrincipal();
					}
				}
				
			}
			else{
				System.out.println("nn instancia");
			}
		} catch (Exception e) {

			this.acessoNegado();
		}

	}
	private String acessoNegado() {
		return "acessonegado";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}