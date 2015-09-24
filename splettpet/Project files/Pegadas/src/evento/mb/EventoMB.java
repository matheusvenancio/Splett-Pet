package evento.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.NoResultException;

import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DualListModel;
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
import organizacao.Organizacao;
import organizacao.dao.OrganizacaoDao;
import organizacao.mb.OrganizacaoMB;

import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import usuario.fb.FacesUtils;
import usuario.fb.FbEventoController;
import animal.Animal;
import arquivo.mb.ArquivoMB;

import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

import endereco.Endereco;
import endereco.mb.EnderecoMB;
import evento.Evento;
import evento.dao.EventoDao;

@ManagedBean(name = "eventoMB")
@ViewScoped
public class EventoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Evento> eventos;
	private Evento evento = new Evento();
	private Endereco endereco = new Endereco();
	private Organizacao organizacao = new Organizacao();
	private Usuario usuario = new Usuario();
	private Animal animal = new Animal();
	
	private List<Animal> animais;
	private DualListModel<Animal> animaisEvento; 
	
	private Date data_inicio_temp;
	private Date data_termino_temp;
	
	@ManagedProperty(value = "#{arquivoMB}")
	private ArquivoMB arquivoMB;

	@ManagedProperty(value = "#{eventoDao}")
	private EventoDao eventoDao;
	
	@ManagedProperty(value = "#{enderecoMB}")
	private EnderecoMB enderecoMB;
	
	@ManagedProperty(value = "#{organizacaoMB}")
	private OrganizacaoMB organizacaoMB;
	
	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;
	
	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;
	

	private FacebookConnectionFactory connectionFactory;

	private int idEvtFace = 1;
	
	private FbEventoController eventoController = new FbEventoController();

	@PostConstruct
	public void init() {
		eventos = eventoDao.listAll();
	}

	public String painaEventos() {
		eventos = eventoDao.listAll();
		return Paginas.PAGINA_EVENTOS;
	}

	public String paginaCadastroEvento() {
		return Paginas.PAGINA_CADASTRO_EVENTO;
	}
	
	public String paginaEvento() {
		return Paginas.PAGINA_EVENTOS;
	}

	public void novo() {
		evento = new Evento();
		endereco = new Endereco();
	}

	public String remover() {
		eventoDao.remover(getEvento());
		eventos = eventoDao.listAll();
		return Paginas.PAGINA_EVENTOS;
	}

	public String cancelar() {
		evento = null;
		return Paginas.PAGINA_EVENTOS;
	}
	
	public void carregarOrgEvt(){
		List<Animal> animaisTarget = new ArrayList<Animal>();
		try {
			organizacao = organizacaoDao.findOrgByMod(findUsrLogado().getId());
		} catch (Exception e) {
			organizacao = organizacaoDao.findOrgByVol(findUsrLogado().getId());
		}
		System.out.println("carregando org evento: " + organizacao.getNome());
		animais = organizacaoMB.listarAnimaisOrg(organizacao.getId());
	
		animaisEvento = new DualListModel<Animal>(animais, animaisTarget);
		
	}

	public String salvar() {
		try {
			(evento).setEndereco(endereco);
			if (evento.getIdPublicacao() > 0) {
				eventoDao.update(evento);
			} else {
				try {
					System.out.println("chamndo try org");
					organizacao = organizacaoDao.findOrgByMod(findUsrLogado().getId());
				} catch (Exception e) {
					
					organizacao = organizacaoDao.findOrgByVol(findUsrLogado().getId());
				}
				
				System.out.println("ong no evento: " + organizacao.getNome());
				evento.setUsuario(findUsrLogado());
				endereco.setEstado(enderecoMB.getEstado());
				endereco.setCidade(enderecoMB.getCidade());
				evento.setEndereco(endereco);
				
				System.out.println("upando img: ");
				if (arquivoMB.getArqSelecionado() == null) {
					try {
						evento.setImgMain(arquivoMB.getImgs().get(0));
					} catch (Exception e) {
						evento.setImgMain(null);
					}
				} else {
					evento.setImgMain(arquivoMB.getArqSelecionado());
				}
			
				evento.setOrganizacao(organizacao);
				
				evento.setAnimais(animaisEvento.getTarget());
				
				eventoDao.salvar(evento);
					
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Paginas.PAGINA_EVENTOS;
	}
	
	public void listarAnimaisEvento(int id){
		animais = eventoDao.listarAnimaisEvt(id);
	}
	
	public void setarDataInicioTemp(FacesContext fc, UIComponent component,
			Object value){
		System.out.println("VALue EVeneto: " + value.toString());
		DateFormat formatter = new SimpleDateFormat("dd/mm/yyyy : HH:mm");  
		try {
			data_inicio_temp = (Date)formatter.parse(value.toString());
		} catch (ParseException e) {
			System.out.println("nao formatou a data");
			e.printStackTrace();
		} 
	}
	
	public boolean validaDataEvento(FacesContext fc, UIComponent component,
			Object value) {
		System.out.println("23");
		UIInput dataEvtInicioComponent = (UIInput) component.getAttributes().get(
                "dataInicioEvento");
        System.out.println("dataEvtInicioComponent" + dataEvtInicioComponent.getValue().toString());
        
		System.out.println("evt value: " + value.toString());
		System.out.println("data inicio: " + evento.getDataInicioEvento());
		System.out.println("data termino: " + evento.getDataTerminoEvento());
		try {
			if (evento.getDataTerminoEvento() == null
					|| evento.getDataInicioEvento().before(evento.getDataTerminoEvento())) {
				System.out.println("erro na validacao do evento");
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro de validação",
						"Hˆ alguma coisa de errado com essas datas!"));
			}
		} catch (NoResultException e) {
			
		}
		return true;
	}
	
	public boolean validarDataEvento(
			Object value) throws ValidatorException{
		
		System.out.println("data inicio: " + evento.getDataInicioEvento());
		System.out.println("data termino: " + value.toString());
		try {
			if (evento.getDataTerminoEvento() == null
					|| evento.getDataInicioEvento().before(evento.getDataTerminoEvento())) {
				System.out.println("erro na validacao do evento");
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro de validação",
						"Hˆ alguma coisa de errado com essas datas!"));
			}
			else{
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro de validacao",
						"Valor nao reconhecido"));
			}
		} catch (NoResultException e) {
		}
		
		return true;
	}
	
	public Usuario findUsrLogado() {
		try {
			usuario = new Usuario();
			SecurityContext context = SecurityContextHolder.getContext();
			if (context instanceof SecurityContext) {
				Authentication authentication = context.getAuthentication();
				if (authentication instanceof Authentication) {
					try {
						
						usuario.setUsername(context.getAuthentication().getPrincipal().toString());
						usuario.setUsername(((User) authentication.getPrincipal())
								.getUsername());
						String username = usuario.getUsername();
						usuario = usuarioDao.findByLogin(username);
					} catch (Exception e) {
						this.usuario = (Usuario) context.getAuthentication().getPrincipal();
						String username = usuario.getUsername();
						usuario = usuarioDao.findByLogin(username);						
					}
				}
				return usuario;
			}
			else{
				System.out.println("nn instancia");
			}
		} catch (Exception e) {
			return null;
		}
		return usuario;
		
	}
	
	
	//FACEBOOK
	
	@RequestMapping(value = "/facebook/feed", method = RequestMethod.GET)
	public void autenticarSpringComFacebook(int id) {
		try {
			System.out.println("ID: " + id);
			pegarEventoFace();
			connectionFactory = new FacebookConnectionFactory(Paginas.APP_ID,
					Paginas.APP_SECRET);
			OAuth2Operations oauthOperations = connectionFactory
					.getOAuthOperations();
			OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();

			oAuth2Parameters.setScope("user_about_me, user_likes,user_status, publish_stream, email, create_event, read_friendlists, rsvp_event, share_item, status_update, user_activities, user_birthday, user_checkins, user_events, friends_events, user_friends, user_location, user_notes, user_online_presence, user_photos, user_status");

			oAuth2Parameters.add("display", "popup");
			oAuth2Parameters
					.setRedirectUri("http://localhost:8080/Pegadas/eventos/confirmaEventoFacebook.ifpr?id="+id);
			String authorizeUrl = oauthOperations.buildAuthorizeUrl(
					GrantType.AUTHORIZATION_CODE, oAuth2Parameters);

			FacesUtils.getExternalContext().redirect(authorizeUrl);
			System.out.println("redirecionando pag evento..");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void criarEvento(Evento evento) {
			try {
			Map<String, String> paramMap = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
			String code = paramMap.get("code");
			String id = paramMap.get("id");
				if (code != null && !code.isEmpty()) {
				FacebookConnectionFactory connFactory = new FacebookConnectionFactory(
						Paginas.APP_ID, Paginas.APP_SECRET);
				String serverPath = FacesUtils.getApplicationURI();
				
				AccessGrant accessGrant = null;
				try {
					accessGrant = connFactory.getOAuthOperations()
							.exchangeForAccess(code, serverPath+"?id="+id, null);
				} catch (Exception e1) {
					System.out.println("e1");
					e1.printStackTrace();
				}
				
			System.out.println("try...");
				  try {
	                    FacebookClient facebookClient = new DefaultFacebookClient(
	                            accessGrant.getAccessToken());
	                    
	                    Date dataFormaterInicio;
	                    Date dataFormaterTermino;
	                    String dataInicioEvento = evento.getDataInicioEvento().toString();
	                    String dataTerminoEvento = evento.getDataTerminoEvento().toString();
	                    
	                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
	                   formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	                    	dataFormaterInicio = formatter.parse(dataInicioEvento);
	                    	dataFormaterTermino = formatter.parse(dataTerminoEvento);
	                    	
	                    	
	                    	
	                    	System.out.println("location: " + evento.getEndereco().getEstado().getNome() + " " +
	                                						evento.getEndereco().getCidade().getNome() + " " + evento.getEndereco().getBairro() + " " + 
	                                								evento.getEndereco().getLogradouro() + " " + evento.getEndereco().getBairro() + " " + 
	                                										evento.getEndereco().getComplemento() + " " + evento.getEndereco().getNumero());
	                    //	
	                    	
	                    	try{
	                    	//	Date tomorrow = new Date(1000L * 60L * 60L * 24L);
	                    	//	Date twoDaysFromNow = new Date(1000L * 60L * 60L * 48L);
	                    	//	System.out.println("data fb inicio: " + tomorrow.toString());
	                    	//	System.out.println("data evento inicio: " + dataFormaterInicio);
	                    	//	System.out.println("data fb termino: " + twoDaysFromNow.toString());
	                    	//	System.out.println("data evento termino: " + dataFormaterTermino.toString());
	                    	//	System.out.println("nome do evento: " + evento.getTitulo());
	                    		//System.out.println("evt img fb: " +  evento.getImgMain().getPath() + evento.getImgMain().getNome());
	                    	
	                    		if(evento.getImgMain() != null){
	                    			
	                    			System.out.println("picture: " + evento.getImgMain().getPath());
	                    			
	                    			
	                    		File file = new File(evento.getImgMain().getPath());
	                    		InputStream inputStream = new FileInputStream(file);
	                    		
	                    		facebookClient.publish("me/events", FacebookType.class, 
	                    				BinaryAttachment.with("cat.png", inputStream),
	                    				Parameter.with("name", evento.getTitulo()), Parameter.with("start_time", dataFormaterInicio),
	   	                                Parameter.with("end_time", dataFormaterTermino),Parameter.with("description", evento.getDescricao()),
	   	                                			Parameter.with("location", evento.getEndereco().getEstado().getNome() + " " +
	   	                                						evento.getEndereco().getCidade().getNome() + " " + evento.getEndereco().getBairro() + " " + 
	   	                                								evento.getEndereco().getLogradouro() + " " + evento.getEndereco().getBairro() + " " + 
	   	                                										evento.getEndereco().getComplemento() + " " + evento.getEndereco().getNumero()), 
	   	                                										Parameter.with("fileUpload", true),
	   	                                										Parameter.with("picture", evento.getImgMain().getPath()));
	                    		
	                    		
	                    		}
	                    		
	                    		else{
	                    			System.out.println("evento nao possui imagem");
	                    			facebookClient.publish("me/events", FacebookType.class, 
		                    				Parameter.with("name", evento.getTitulo()), Parameter.with("start_time", dataFormaterInicio),
		   	                                Parameter.with("end_time", dataFormaterTermino),Parameter.with("description", evento.getDescricao()),
		   	                                			Parameter.with("location", evento.getEndereco().getEstado().getNome() + " " +
		   	                                						evento.getEndereco().getCidade().getNome() + " " + evento.getEndereco().getBairro() + " " + 
		   	                                								evento.getEndereco().getLogradouro() + " " + evento.getEndereco().getBairro() + " " + 
		   	                                										evento.getEndereco().getComplemento() + " " + evento.getEndereco().getNumero()));
	                    		}
	                    		
	                    		/*	
	                    		TemplatizedAction action = new TemplatizedAction("me/events");       
	                    		action.addPicture(evento.getImgMain().getPath());
	                    		FacebookJsonRestClient client = new FacebookJsonRestClient(Paginas.APP_ID, Paginas.APP_SECRET);
	                    		client.feed_PublishTemplatizedAction(action);
	                    	*/
	                    		/*
	                    		MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
		                        data.set("name", evento.getTitulo());
		                        data.set("start_time", dataFormaterInicio);
		                        data.set("end_time", dataFormaterTermino);
		                        data.set( "@" + evento.getImgMain().getNome(), "@" + evento.getImgMain().getPath());  
		                        
	                    		FacebookTemplate facebookTemplate = new FacebookTemplate();
	                    		facebookTemplate.publish("me", "events", data);
	                    		
	                    		*/
	                    		 
	                    	}catch (Exception e) {
								System.out.println("nao criou evento no facebook");
								e.printStackTrace();
							}
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
			}
		} catch (Exception e) {
			System.out.println("NAO CRIOU");
			e.printStackTrace();
		}
	}
	
	//http://restfb.com/
	
	
	public void pegarEventoFace(){
		evento = eventoDao.findById(idEvtFace);
	}

	public void handleToggle(ToggleEvent event) {  
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fieldset Toggled", "Visibility:" + event.getVisibility());  
  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
	
	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public Evento getEvento() {
		return (Evento) evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public EventoDao getEventoDao() {
		return eventoDao;
	}

	public void setEventoDao(EventoDao eventoDao) {
		this.eventoDao = eventoDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

	public EnderecoMB getEnderecoMB() {
		return enderecoMB;
	}

	public void setEnderecoMB(EnderecoMB enderecoMB) {
		this.enderecoMB = enderecoMB;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public FbEventoController getEventoController() {
		return eventoController;
	}

	public void setEventoController(FbEventoController eventoController) {
		this.eventoController = eventoController;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public OrganizacaoMB getOrganizacaoMB() {
		return organizacaoMB;
	}

	public void setOrganizacaoMB(OrganizacaoMB organizacaoMB) {
		this.organizacaoMB = organizacaoMB;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
	}

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
	}

	public FacebookConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(FacebookConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public int getIdEvtFace() {
		return idEvtFace;
	}

	public void setIdEvtFace(int idEvtFace) {
		this.idEvtFace = idEvtFace;
	}

	public ArquivoMB getArquivoMB() {
		return arquivoMB;
	}

	public void setArquivoMB(ArquivoMB arquivoMB) {
		this.arquivoMB = arquivoMB;
	}


	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public DualListModel<Animal> getAnimaisEvento() {
		return animaisEvento;
	}

	public void setAnimaisEvento(DualListModel<Animal> animaisEvento) {
		this.animaisEvento = animaisEvento;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Date getData_inicio_temp() {
		return data_inicio_temp;
	}

	public void setData_inicio_temp(Date data_inicio_temp) {
		this.data_inicio_temp = data_inicio_temp;
	}

	public Date getData_termino_temp() {
		return data_termino_temp;
	}

	public void setData_termino_temp(Date data_termino_temp) {
		this.data_termino_temp = data_termino_temp;
	}

	
	
}