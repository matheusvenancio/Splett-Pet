package usuario.atividadeUsuario;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import usuario.Usuario;
import usuario.atividadeUsuario.dao.AtividadeUsuarioDao;
import usuario.dao.UsuarioDao;

@ManagedBean(name = "atividadeUsuarioMB")
@ViewScoped
public class AtividadeUsuarioMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private AtividadeUsuario atividadeUsuario = new AtividadeUsuario();

	@ManagedProperty(value = "#{atividadeUsuarioDao}")
	private AtividadeUsuarioDao atividadeUsuarioDao;

	private Usuario usuario;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	public void salvatAT(Usuario usuarioLogado) {
		try {
			atividadeUsuario = atividadeUsuarioDao
					.findAtividadeUsuarioById(usuarioLogado.getId());
			if (atividadeUsuario.getId() > 0) {
				atividadeUsuario.setDataLogin(new java.sql.Date(System
						.currentTimeMillis()));
				atividadeUsuarioDao.update(atividadeUsuario);
			}
		} catch (Exception e) {
			atividadeUsuario.setUsuario(usuarioLogado);
			atividadeUsuario.setDataLogin(new java.sql.Date(System
					.currentTimeMillis()));
			atividadeUsuarioDao.salvar(atividadeUsuario);
		}
	}

	public void sair() {
		usuario = getUsrLogado();
		atividadeUsuario = atividadeUsuarioDao.findAtividadeUsuarioById(usuario
				.getId());
		System.out.println("usuario: " + atividadeUsuario.getUsuario().getNome());
		atividadeUsuario.setDataLogof((new java.sql.Date(System
				.currentTimeMillis())));
		atividadeUsuarioDao.update(atividadeUsuario);
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/Pegadas/j_spring_security_logout");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Usuario getUsrLogado() {
		try {
			usuario = new Usuario();
			SecurityContext context = SecurityContextHolder.getContext();
			if (context instanceof SecurityContext) {
				Authentication authentication = context.getAuthentication();
				if (authentication instanceof Authentication) {
					try {

						usuario.setUsername(context.getAuthentication()
								.getPrincipal().toString());
						usuario.setUsername(((User) authentication
								.getPrincipal()).getUsername());
						String username = usuario.getUsername();
						usuario = usuarioDao.findByLogin(username);
					} catch (Exception e) {
						this.usuario = (Usuario) context.getAuthentication()
								.getPrincipal();
						String username = usuario.getUsername();
						usuario = usuarioDao.findByLogin(username);
					}
				}
				return usuario;
			} else {
				System.out.println("nn instancia");
			}
		} catch (Exception e) {
			return null;
		}
		return usuario;

	}

	public AtividadeUsuario getAtividadeUsuario() {
		return atividadeUsuario;
	}

	public void setAtividadeUsuario(AtividadeUsuario atividadeUsuario) {
		this.atividadeUsuario = atividadeUsuario;
	}

	public AtividadeUsuarioDao getAtividadeUsuarioDao() {
		return atividadeUsuarioDao;
	}

	public void setAtividadeUsuarioDao(AtividadeUsuarioDao atividadeUsuarioDao) {
		this.atividadeUsuarioDao = atividadeUsuarioDao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

}
