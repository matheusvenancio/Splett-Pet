package usuario.fb;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.primefaces.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import usuario.mb.SenhaMd5MB;

import com.visural.common.IOUtil;

import endereco.Endereco;

@ManagedBean(name = "conectarContaController")
@SessionScoped
public class ConectarContaFb implements Serializable {

	public static final String apiKey = "139428799581057";
	public static final String secretKey = "8e7187de42b226da875fb70f7c991e12";

	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();
	private SenhaMd5MB senhaMd5 = new SenhaMd5MB();

	private boolean isUsuarioFacebook = false;
	private FacebookConnectionFactory connectionFactory;
	
	
	private List<Usuario> usuarios;
	private boolean mostrarPanelConnFace;
	//@ManagedProperty(value = "#{enderecoMB}")
	//private EnderecoMB enderecoMB;


	private Endereco endereco = new Endereco();

	private boolean isDadosCompativeis = false;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	private String nome = "a";
	private String sobrenome = "b";
	private String email = "c";
	
	private int activeTab;

	@PostConstruct
	public void init() {
		usuarios = usuarioDao.listAll();
	}
	
	
	
	@RequestMapping(value = "/facebook/feed", method = RequestMethod.GET)
	public void autenticarSpringComFacebook() {
		try {
			connectionFactory = new FacebookConnectionFactory(Paginas.APP_ID,
					Paginas.APP_SECRET);
			OAuth2Operations oauthOperations = connectionFactory
					.getOAuthOperations();
			OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();

			oAuth2Parameters
					.setScope("user_about_me, user_likes,user_status, publish_stream, email, create_event, read_friendlists, rsvp_event, share_item, status_update, user_activities, user_birthday, user_checkins, user_events, user_friends, user_location, user_notes, user_online_presence, user_photos, user_status");
			oAuth2Parameters.add("display", "popup");
			oAuth2Parameters
					.setRedirectUri("http://localhost:8080/Pegadas/usuarios/configuracoes.ifpr");
			String authorizeUrl = oauthOperations.buildAuthorizeUrl(
					GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
			System.out.println("URL: " + authorizeUrl.toString());

			FacesUtils.getExternalContext().redirect(authorizeUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void conectarConta() {
		try {
			pegarUsrSessao();
			Map<String, String> paramMap = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			String code = paramMap.get("code");

			if (code != null && !code.isEmpty()) {
				FacebookConnectionFactory connFactory = new FacebookConnectionFactory(
						Paginas.APP_ID, Paginas.APP_SECRET);
				String serverPath = FacesUtils.getApplicationURI();
				AccessGrant accessGrant = connFactory.getOAuthOperations()
						.exchangeForAccess(code, serverPath, null);

				try {
					JSONObject resp = new JSONObject(IOUtil.urlToString(new URL(
							"https://graph.facebook.com/me?access_token="
									+ accessGrant.getAccessToken())));

					if (usuarioDao.findByEmail(resp.getString("email")) != null) {
						usuario = usuarioDao.findByEmail(resp.getString("email"));
					}
					
					else{
						System.out.println("usuario Ž nulo");
					}
					
					if (usuario.getNome() != resp.getString("first_name")
							|| usuario.getSobrenome() != resp
									.getString("last_name")
							|| usuario.getEmail() != resp.getString("email")) {

						
						setarDadosComPativeisFalse();
						
						nome = resp.getString("first_name");
						sobrenome = (resp.getString("last_name"));
						email = (resp.getString("email"));
						int id = usuario.getId();
						try {
							usuario.setNome(nome);
							usuario.setSobrenome(sobrenome);
							usuario.setEmail(email);
							usuarios = usuarioDao.listAll();
							System.out.println("nome usr: " + usuario.getNome());
							System.out.println("lista usr: " + usuarios.get(0).getNome());
							usuarios.set((id-1), usuario);
							
						} catch (Exception e) {
							System.out
									.println("n‹o conseguiu setar as informa›es do usuario");
							e.printStackTrace();
						}
						setMostrarPanelConnFace(true);
						activeTab = 5;
					}
					
					if(usuario.getBio() == null){
						try {
							usuario.setBio(resp.getString("bio"));
						} catch (Exception e) {
							// TODO: handle exception
						}						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("exception conn");
		}
	}

	public void pegarUsrSessao() {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			if (context instanceof SecurityContext) {
				Authentication authentication = context.getAuthentication();
				if (authentication instanceof Authentication) {
					try {
						usuario.setUsername(((User) authentication
								.getPrincipal()).getUsername());
						String username = usuario.getUsername();

						usuario = usuarioDao.findByLogin(username);
						System.out.println("usr - connContaFb: "
								+ usuario.getNome());
					} catch (Exception e) {
						System.out
								.println("metodo principal - pegarUsrSessao - ConectarContaFb");
						this.usuario = (Usuario) context.getAuthentication()
								.getPrincipal();
					}
				}

			} else {
				System.out.println("nn instancia");
			}
		} catch (Exception e) {
			;
		}

	}


	public String updateUsuarioFace()	throws ValidatorException {
		if (usuario.getId() > 0) {
		try {
			
			System.out.println("update usr face: " + usuario.getNome());
			
			usuario.setEndereco(endereco);
			usuario.setFacebook(true);
			usuarioDao.update(usuario);
			System.out.println("depois do update: " + usuario.getNome());
			setActiveTab(0);
			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			return "index";
		}
		}else{
			System.out.println("id < 0");
			return "index";
		}
	}

	public void validaDadosFace() throws ValidatorException {
		throw new ValidatorException(
				new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Alguns dados seus n‹o batem com os do Facebook e precisam ser trocados",
						" Voc concprda com estas altera›es?"));
	}

	public void setarDadosComPativeisFalse() {
		isDadosCompativeis = false;
	}

	public void setarDadosComPativeisTrue() {
		isDadosCompativeis = true;
	}

	public FacebookConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(FacebookConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isUsuarioFacebook() {
		return isUsuarioFacebook;
	}

	public void setUsuarioFacebook(boolean isUsuarioFacebook) {
		this.isUsuarioFacebook = isUsuarioFacebook;
	}

	public SenhaMd5MB getSenhaMd5() {
		return senhaMd5;
	}

	public void setSenhaMd5(SenhaMd5MB senhaMd5) {
		this.senhaMd5 = senhaMd5;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public boolean isDadosCompativeis() {
		return isDadosCompativeis;
	}

	public void setDadosCompativeis(boolean isDadosCompativeis) {
		this.isDadosCompativeis = isDadosCompativeis;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}



	public boolean isMostrarPanelConnFace() {
		return mostrarPanelConnFace;
	}



	public void setMostrarPanelConnFace(boolean mostrarPanelConnFace) {
		this.mostrarPanelConnFace = mostrarPanelConnFace;
	}




	
}
