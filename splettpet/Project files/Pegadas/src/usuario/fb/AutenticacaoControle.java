package usuario.fb;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import preferencias.Preferencias;
import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import usuario.mb.SenhaMd5MB;

import arquivo.dao.ArquivoDao;

import com.visural.common.IOUtil;

import endereco.Endereco;

@ManagedBean(name = "autenticacaoController")
@SessionScoped
public class AutenticacaoControle implements Serializable {

	public static final String apiKey = "139428799581057";
	public static final String secretKey = "8e7187de42b226da875fb70f7c991e12";

	private static final long serialVersionUID = 1L;
	private FacebookConnectionFactory connectionFactory;
	private Usuario usuario = new Usuario();
	
	private Facebook facebook;
	private Endereco endereco = new Endereco();
	private int i = 0;
	int j = 0;
	private boolean isUsuarioFacebook = false;
	
	private SenhaMd5MB senhaMd5 = new SenhaMd5MB();
	OAuth2Parameters oAuth2Parameters;
	JSONObject resp;
	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;
	
	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	

	private Preferencias preferencias = new Preferencias();
	

	public void trocaNumero() throws IOException, ServletException,
			JSONException {
			processLoginFacebook();
		
	}

	
	@RequestMapping(value="/facebook/feed", method=RequestMethod.GET)
	public void autenticarSpringComFacebook() {
		try {
			connectionFactory = new FacebookConnectionFactory(Paginas.APP_ID, Paginas.APP_SECRET);
			OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
			OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();
			
			oAuth2Parameters.setScope("user_about_me, user_likes,user_status, publish_stream, email, create_event, read_friendlists, rsvp_event, share_item, status_update, user_activities, user_birthday, user_checkins, user_events, friends_events, user_friends, user_location, user_notes, user_online_presence, user_photos, user_status");
			oAuth2Parameters.add("display", "popup");
			oAuth2Parameters.setRedirectUri("http://localhost:8080/Pegadas/index.ifpr");
			String authorizeUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
		 	FacesUtils.getExternalContext().redirect(authorizeUrl);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(value="/facebook/feed", method=RequestMethod.GET)
	public String processLoginFacebook() throws IOException, ServletException {
		if (i == 0) {
			try {
				System.out.println("aut: " +  FacesContext.getCurrentInstance().getExternalContext());
				Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
				System.out.println("param aut: " + paramMap.toString());
				String code = paramMap.get("code");
				
				if (code != null && !code.isEmpty()) {
					FacebookConnectionFactory connFactory = new FacebookConnectionFactory(Paginas.APP_ID, Paginas.APP_SECRET);
					String serverPath = FacesUtils.getApplicationURI();
					AccessGrant accessGrant = connFactory.getOAuthOperations().exchangeForAccess(code, serverPath, null);
					
					
					try {
						JSONObject resp = new JSONObject(
								IOUtil.urlToString(new URL(
										"https://graph.facebook.com/me?access_token="
												+ accessGrant.getAccessToken())));
						String email = resp.getString("email");
						
						try {
								if (usuarioDao.findByEmail(email) != null) {
								usuario = usuarioDao.findByEmail(email);
								authenticate(usuario);
								SenhaMd5MB senhaMd5MB = new SenhaMd5MB();
								
								UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
							        		usuario.getEmail(), senhaMd5MB.criptografar(usuario.getPassword()));
							        		token.setDetails(new WebAuthenticationDetails(FacesUtils.getServletRequest()));
								Authentication authentication = usuarioDao.authenticate(token);
								
								
								SecurityContextHolder.getContext().setAuthentication(authentication);
							    FacesUtils.getServletRequest().getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
							                 SecurityContextHolder.getContext()); 
							  
							  
								try {
									FacesUtils.getExternalContext().redirect("index.ifpr");
									} catch (Exception ex) {
									;
								}
							}else{
								criaNovoUsuarioFace(accessGrant, resp);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception ev) {
						ev.printStackTrace();
					}
					 
				}
				
			} finally {
				return "index";
			}
		}
		return "index";
	}

	
	protected void authenticate(Usuario usuario){
	    SecurityContextHolder.getContext().getAuthentication();
	    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getPassword());
	    token.setDetails(new WebAuthenticationDetails(FacesUtils.getServletRequest()));
	    Authentication authentication = usuarioDao.authenticate(token);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
	}
	
	public boolean validarLogin(FacesContext fc, UIComponent component,
			Object value) {
		try {
			if (usuario.getUsername() == null
					|| !usuario.getUsername().equals(value.toString())) {
				usuarioDao.findByLogin(value.toString());
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro de validação",
						"Login já cadastrado"));
			}
		} catch (NoResultException e) {
		}
		return true;
	}
	
	public void criaNovoUsuarioFace (AccessGrant accessGrant, JSONObject resp)
			throws JSONException, IOException, ServletException {
		
	 
		
		try {
				resp = new JSONObject(IOUtil.urlToString(new URL(
						"https://graph.facebook.com/me?access_token="
								+ accessGrant.getAccessToken())));
				
			 	try {
					usuario.setEmail(resp.getString("email"));
					usuario.setNome(resp.getString("first_name"));
					usuario.setSobrenome(resp.getString("last_name"));
					
					try {
					//	usuario.setSexo(resp.getString("gender"));
					} catch (Exception e) {
						//usuario.setSexo(null);
					}
					try {
						usuario.setBio(resp.getString("bio"));
					} catch (Exception e) {
						usuario.setBio(null);
					}
					try {
						usuario.setTelefoneCel(resp.getString("phone")); 
					} catch (Exception e) {
						usuario.setTelefoneCel(null);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/Pegadas/fb/facebookCadastro.ifpr");
			} catch (Exception e) {
				System.out.println("ERRO 258");
				
				e.printStackTrace();
			}		
	}


	public String salvarUsuarioFace(){
		try {
			usuario.setPassword(senhaMd5.criptografar(usuario.getPassword()));
			usuario.setEndereco(endereco);
			usuario.setAuthority("ROLE_USER");
			usuario.setFacebook(true);
			usuario.setAvatar(arquivoDao.findById(1));
			setarPreferencias();
			usuario.setPreferencias(preferencias);
			usuarioDao.salvar(usuario);
			processLoginFacebook();
			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			return "index";
		}
		
	}
	
	public void setarPreferencias() {
		preferencias.setContaConfirmada(true);
		preferencias.setCodValidaEmail(senhaMd5.criptografar(usuario.getEmail()
				+ usuario.getDataCadastro()));
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

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public Facebook getFacebook() {
		return facebook;
	}

	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public SenhaMd5MB getSenhaMd5() {
		return senhaMd5;
	}

	public void setSenhaMd5(SenhaMd5MB senhaMd5) {
		this.senhaMd5 = senhaMd5;
	}
	public boolean isUsuarioFacebook() {
		return isUsuarioFacebook;
	}
	public void setUsuarioFacebook(boolean isUsuarioFacebook) {
		this.isUsuarioFacebook = isUsuarioFacebook;
	}


	public Preferencias getPreferencias() {
		return preferencias;
	}


	public void setPreferencias(Preferencias preferencias) {
		this.preferencias = preferencias;
	}


	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}


	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}
	


	
}
