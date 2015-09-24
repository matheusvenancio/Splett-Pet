package usuario.fb;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import usuario.mb.SenhaMd5MB;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

import endereco.Endereco;
import evento.Evento;
import evento.dao.EventoDao;

@ManagedBean(name = "eventoController")
@Named("criarEvento")
@Controller
@Scope("session")
public class FbEventoController {

	private Usuario usuario = new Usuario();
	private SenhaMd5MB senhaMd5 = new SenhaMd5MB();

	private FacebookConnectionFactory connectionFactory;

	private List<Usuario> usuarios;

	private Endereco endereco = new Endereco();
	private static Logger logger = Logger.getLogger(Evento.class.getName());
	private boolean skip;
	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{eventoDao}")
	private EventoDao eventoDao;

	private Evento evento = new Evento();
	int id = 0;

	@RequestMapping(value = "/facebook/feed", method = RequestMethod.GET)
	public void autenticarSpringComFacebook(Evento evento) {
		System.out.println("sprgFb: " + evento.getTitulo());
		try {
			connectionFactory = new FacebookConnectionFactory(Paginas.APP_ID,
					Paginas.APP_SECRET);
			OAuth2Operations oauthOperations = connectionFactory
					.getOAuthOperations();
			OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();

			oAuth2Parameters
					.setScope("create_event, read_friendlists, user_events");
			oAuth2Parameters.add("display", "popup");
			oAuth2Parameters
					.setRedirectUri("http://localhost:8080/Pegadas/eventos/confirmaEventoFacebook.ifpr");
			String authorizeUrl = oauthOperations.buildAuthorizeUrl(
					GrantType.AUTHORIZATION_CODE, oAuth2Parameters);

			FacesUtils.getExternalContext().redirect(authorizeUrl);
			System.out.println("83..");
			// criarEvento(evento);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public void salvarEvento(){
	//	HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	//	System.out.println("orig: " + origRequest.toString());
		System.out.println("eveveve: " + evento.getTitulo());
		try {
			(evento).setEndereco(endereco);
			if (evento.getIdPublicacao() > 0) {
				eventoDao.update(evento);
			} else {
				eventoDao.salvar(evento);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		id = evento.getIdPublicacao();
		System.out.println("id: " + id);
		//autenticarSpringComFacebook(evento);
		
	}
	public Evento findEventoById(){
		evento = eventoDao.findById(id);
		System.out.println("evento nome: " + evento.getTitulo());
		criarEvento(evento);
		return evento;
	}
	
	public void criarEvento(Evento evento) {
		System.out.println("90");


		try {
			Map<String, String> paramMap = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			System.out.println("param: " + paramMap.toString());
			System.out.println("corruent instance: "
					+ FacesContext.getCurrentInstance().getExternalContext().getContextName());
			String code = paramMap.get("code");
			System.out.println("code: " + code.toString());
			if (code != null && !code.isEmpty()) {
				FacebookConnectionFactory connFactory = new FacebookConnectionFactory(
						Paginas.APP_ID, Paginas.APP_SECRET);
				String serverPath = FacesUtils.getApplicationURI();
				AccessGrant accessGrant = connFactory.getOAuthOperations()
						.exchangeForAccess(code, serverPath, null);
				System.out.println("indo salvar :D");
				
				
				try {

					// Evento evento = new Evento();
					/*JSONObject resp = new JSONObject(
							IOUtil.urlToString(new URL(
									"https://graph.facebook.com/me/events?access_token="
											+ accessGrant.getAccessToken())));*/

					System.out.println("restFb: ");
					FacebookClient facebookClient = new DefaultFacebookClient(
							accessGrant.getAccessToken());
					FacebookType publishEventResponse = facebookClient.publish(
							"me/events",
							FacebookType.class,
							Parameter.with("name", evento.getTitulo()),
							Parameter.with("start_time",
									evento.getDataInicioEvento()),
							Parameter.with("end_time",
									evento.getDataTerminoEvento()));

					System.out.println("teste evento: "
							+ publishEventResponse.getId());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("NAO CRIOU");
			e.printStackTrace();
		}
	}

	public String onFlowProcess(FlowEvent event) {  
        logger.info("Current wizard step:" + event.getOldStep());  
        logger.info("Next step:" + event.getNewStep());  
          
        if(skip) {  
            skip = false;
            return "confirm";  
        }  
        else {  
            return event.getNewStep();  
        }  
    }  
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public SenhaMd5MB getSenhaMd5() {
		return senhaMd5;
	}

	public void setSenhaMd5(SenhaMd5MB senhaMd5) {
		this.senhaMd5 = senhaMd5;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public EventoDao getEventoDao() {
		return eventoDao;
	}

	public void setEventoDao(EventoDao eventoDao) {
		this.eventoDao = eventoDao;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}


	public static Logger getLogger() {
		return logger;
	}


	public static void setLogger(Logger logger) {
		FbEventoController.logger = logger;
	}


	public boolean isSkip() {
		return skip;
	}


	public void setSkip(boolean skip) {
		this.skip = skip;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	

}
