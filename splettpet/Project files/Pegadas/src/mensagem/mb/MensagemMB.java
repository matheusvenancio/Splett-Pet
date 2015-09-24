package mensagem.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import mensagem.Mensagem;
import mensagem.MensagemDataModel;
import mensagem.dao.MensagemDao;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import arquivo.Arquivo;
import arquivo.dao.ArquivoDao;
import arquivo.mb.ArquivoMB;

@ManagedBean(name = "mensagemMB")
@ViewScoped
public class MensagemMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Mensagem> mensagens;
	private Mensagem mensagem = new Mensagem();

	private Usuario usuario = new Usuario();

	private String usernameDest;
	private Usuario usrDest;
	private List<Usuario> usuarios;
	private List<Usuario> usuariosSelecionados;

	private String loginDestinatario;
	private List<Arquivo> anexos = new ArrayList<Arquivo>();

	@ManagedProperty(value = "#{mensagemDao}")
	private MensagemDao mensagemDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{arquivoMB}")
	private ArquivoMB arquivoMB;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private MensagemDataModel msgs;

	private boolean isMsgSelecionada;
	private boolean isResponder;

	@PostConstruct
	public void init() {
		usuarios = usuarioDao.listAll();
		loadMensagens();
	}

	public void novo() {
		mensagem = new Mensagem();
	}

	public String paginaEnviarMensagem() {
		return Paginas.PAGINA_ENVIAR_MENSAGEM;
	}

	public String paginaMensagens() {
		return Paginas.PAGINA_MENSAGENS;
	}

	public String remover() {
		mensagemDao.remover(mensagem);
		mensagens = mensagemDao.listAll();
		return Paginas.PAGINA_MENSAGENS;
	}

	public String cancelar() {
		mensagem = null;
		return Paginas.PAGINA_MENSAGENS;
	}

	public List<Usuario> usuariosDoSistema() {
		usuarios = usuarioDao.listAll();
		return usuarios;
	}

	public String salvar() {
		mensagem.setRemetente(findUsuarioLogado());
		usrDest = usuarioDao.findByLogin(loginDestinatario);
		mensagem.setDestinatario(usrDest);
		System.out.println("nome dest: " + usrDest.getNome());
		mensagem.setLida(false);
		mensagem.setArquivos(arquivoMB.getFiles());

		mensagemDao.salvar(mensagem);
		return Paginas.PAGINA_MENSAGENS;
	}
	
	public String salvarMensagemT() {
		mensagem.setRemetente(findUsuarioLogado());
		mensagem.setDestinatario(usrDest);
		System.out.println("nome dest: " + usrDest.getNome());
		mensagem.setLida(false);
		mensagem.setArquivos(arquivoMB.getFiles());

		mensagemDao.salvar(mensagem);
		return Paginas.PAGINA_MENSAGENS;
	}

	public void loadNew() {
		usrDest = usuarioDao.findByLogin(usernameDest);
		System.out.println("123: " + usrDest.getUsername());
	}

	public void loadMensagens() {
		usuario = findUsuarioLogado();
		findCaixaEntrada();
	}

	public void onRowSelect(SelectEvent event) {
		anexos = arquivoDao.findArquivosByMsg(mensagem.getId());
		if (!mensagem.isLida() && mensagem.getRemetente() != usuario) {
			mensagem.setLida(true);
			mensagemDao.update(mensagem);
		}
		isMsgSelecionada = true;
	}

	public void findDestinatarioByLogin() {
		System.out.println("No FinD");
		System.out.println("login : " + loginDestinatario);
	}

	public List<Usuario> completeUsuario(String query) {
		List<Usuario> suggestions = new ArrayList<Usuario>();
		for (Usuario usuario : usuarios) {
			if (usuario.getNome().toLowerCase().substring(0).startsWith(query.toLowerCase())) {
				suggestions.add(usuario);
			}
			
		}
		return suggestions;
	}

	public void handleUnselect(UnselectEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Unselected:" + event.getObject().toString(), null);

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onRowUnselect(UnselectEvent event) {
		anexos = null;
	}

	public List<Arquivo> listarArquivosByMsg(int id) {
		anexos = arquivoDao.findArquivosByMsg(id);
		return anexos;
	}

	public void findCaixaEntrada() {
		try {
			mensagens = mensagemDao.getCaixaEntrada(usuario);
			msgs = new MensagemDataModel(mensagens);
			isMsgSelecionada = false;
		} catch (Exception e) {
			System.out.println("ex mensagem de entrada");
		}
	}

	public void findCaixaSaida() {
		mensagens = mensagemDao.getCaixaSaida(usuario);
		msgs = new MensagemDataModel(mensagens);
		isMsgSelecionada = false;
	}

	public Usuario findUsuarioLogado() {
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

	public int getNumeroNovasMsgs() {
		List<Mensagem> novasMsgsByUsr = new ArrayList<Mensagem>();
		novasMsgsByUsr = mensagemDao.getMsgsNovas(usuario);
		return novasMsgsByUsr.size();
	}

	public void responderMsg() {
		isResponder = true;
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}

	public Mensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

	public MensagemDao getMensagemDao() {
		return mensagemDao;
	}

	public void setMensagemDao(MensagemDao mensagemDao) {
		this.mensagemDao = mensagemDao;
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

	public String getUsernameDest() {
		return usernameDest;
	}

	public void setUsernameDest(String usernameDest) {
		this.usernameDest = usernameDest;
	}

	public Usuario getUsrDest() {
		return usrDest;
	}

	public void setUsrDest(Usuario usrDest) {
		this.usrDest = usrDest;
	}

	public MensagemDataModel getMsgs() {
		return msgs;
	}

	public void setMsgs(MensagemDataModel msgs) {
		this.msgs = msgs;
	}

	public Boolean getIsResponder() {
		return isResponder;
	}

	public void setIsResponder(Boolean isResponder) {
		this.isResponder = isResponder;
	}

	public boolean isMsgSelecionada() {
		return isMsgSelecionada;
	}

	public void setMsgSelecionada(boolean isMsgSelecionada) {
		this.isMsgSelecionada = isMsgSelecionada;
	}

	public void setResponder(boolean isResponder) {
		this.isResponder = isResponder;
	}

	public ArquivoMB getArquivoMB() {
		return arquivoMB;
	}

	public void setArquivoMB(ArquivoMB arquivoMB) {
		this.arquivoMB = arquivoMB;
	}

	public List<Arquivo> getAnexos() {
		return anexos;
	}

	public void setAnexos(List<Arquivo> anexos) {
		this.anexos = anexos;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getLoginDestinatario() {
		return loginDestinatario;
	}

	public void setLoginDestinatario(String loginDestinatario) {
		this.loginDestinatario = loginDestinatario;
	}

	public List<Usuario> getUsuariosSelecionados() {
		return usuariosSelecionados;
	}

	public void setUsuariosSelecionados(List<Usuario> usuariosSelecionados) {
		this.usuariosSelecionados = usuariosSelecionados;
	}

}
