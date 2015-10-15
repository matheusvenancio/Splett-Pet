package splett.fb;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import splett.criptografia.Criptografia;
import splett.usuario.TipoUsuario;
import splett.usuario.Usuario;
import splett.usuario.dao.UsuarioDao;
import splett.util.Constantes;
import splett.util.Funcoes;

@ManagedBean(name = "autenticacaoControle")
@SessionScoped
public class AutenticacaoControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private FacebookConnectionFactory connectionFactory;
	private Usuario usuario;

	private Facebook facebook;
	private boolean exibeCriaContaFacebook = true;

	private Criptografia criptografia = new Criptografia();

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	public void autenticar() {
		try {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ctx.getExternalContext().dispatch("/j_spring_security_check");
			ctx.responseComplete();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@RequestMapping(value="/facebook/feed", method=RequestMethod.GET)
	public void autenticarSpringComFacebook() {

		try {
			connectionFactory = new FacebookConnectionFactory(Constantes.APP_ID, Constantes.APP_SECRET);
			OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
			OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();
			
			oAuth2Parameters.setScope("email, user_birthday");
			oAuth2Parameters.add("display", "popup");
			oAuth2Parameters.setRedirectUri("http://localhost:8080/splettpet/user/perfil.splett");
			String authorizeUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
		 	FacesUtils.getExternalContext().redirect(authorizeUrl);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void processLoginFacebook() throws IOException, ServletException {

		try {

			Map<String, String> paramMap = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			String code = paramMap.get("code");

			if (code != null && !code.isEmpty()) {

				FacebookConnectionFactory connFactory = new FacebookConnectionFactory(
						Constantes.APP_ID, Constantes.APP_SECRET);
				String serverPath = FacesUtils.getApplicationURI()
						+ Constantes.PAGINA_AUTENTICACAO_LOGIN;
				AccessGrant accessGrant = connFactory.getOAuthOperations()
						.exchangeForAccess(code, serverPath, null);
				Connection<Facebook> connection = connFactory
						.createConnection(accessGrant);
				this.facebook = connection.getApi();

				if (this.facebook.isAuthorized()) {

					FacebookProfile fp = facebook.userOperations()
							.getUserProfile();

					if (fp.getUsername() != null && !fp.getUsername().isEmpty()) {
						this.usuario = usuarioDao.pesquisarPorEmail(fp
								.getEmail());

						if (this.usuario == null) {
							criaNovoUsuario(fp, fp.getUsername());
						} else {
							usuarioDao.realizaAutenticacaoAutomatica(
									FacesUtils.getServletRequest(),
									this.usuario);
							FacesUtils.getServletResponse().sendRedirect(
									"http://localhost:8080/splettpet/user/perfil.splett");
						}
					} else {
						String email = Funcoes.recuperaNomeUsuario(fp
								.getEmail());
						this.usuario = usuarioDao.pesquisarPorEmail(email);
						if (usuario == null) {
							criaNovoUsuario(fp, email);
						}
					}
				}
			}
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void criaNovoUsuario(FacebookProfile fp, String email) {
		if (this.usuario == null) {
			this.usuario = new Usuario();
			this.usuario.setEmail(fp.getEmail());
			this.usuario.setNome(fp.getName());
			this.usuario.setDataNascimento(Funcoes.formataStringEmData(fp
					.getBirthday()));
		}
	}

	public void salvarUsuarioFacebook() {

		try {
			usuario.setSenha(criptografia.criptografar(usuario.getSenha()));
			usuario.setTipo(TipoUsuario.ROLE_USER);
			usuario.setFacebook(true);
			usuarioDao.salvar(usuario);
			autenticar();

		} catch (Exception e) {
			setExibeCriaContaFacebook(true);
			e.printStackTrace();
		}
	}
	

	public boolean isExibeCriaContaFacebook() {
		return exibeCriaContaFacebook;
	}

	public void setExibeCriaContaFacebook(boolean exibeCriaContaFacebook) {
		this.exibeCriaContaFacebook = exibeCriaContaFacebook;
	}

	public Usuario getUsuarioAutenticado() {
		return usuarioDao.recoverAuthenticatedUser();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public FacebookConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(FacebookConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Facebook getFacebook() {
		return facebook;
	}

	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}

	public Criptografia getCriptografia() {
		return criptografia;
	}

	public void setCriptografia(Criptografia criptografia) {
		this.criptografia = criptografia;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
