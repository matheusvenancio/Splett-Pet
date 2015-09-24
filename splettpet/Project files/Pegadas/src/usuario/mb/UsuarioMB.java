package usuario.mb;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.NoResultException;
import javax.security.sasl.AuthenticationException;

import org.primefaces.event.FlowEvent;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import organizacao.Organizacao;
import organizacao.dao.OrganizacaoDao;

import preferencias.Preferencias;
import publicacao.Publicacao;
import sistema.Paginas;
import usuario.Usuario;
import usuario.UsuarioController;
import usuario.atividadeUsuario.AtividadeUsuario;
import usuario.atividadeUsuario.AtividadeUsuarioMB;
import usuario.atividadeUsuario.dao.AtividadeUsuarioDao;
import usuario.dao.UsuarioDao;
import arquivo.dao.ArquivoDao;
import arquivo.mb.ArquivoMB;
import email.mb.EmailMB;
import endereco.Endereco;
import endereco.mb.EnderecoMB;

@ManagedBean(name = "usuarioMB")
@ViewScoped
public class UsuarioMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Usuario> usuarios;
	private Usuario usuario = new Usuario();
	private Endereco endereco = new Endereco();
	private Preferencias preferencias = new Preferencias();
	private Usuario usuarioLogado;
	private Usuario usuarioSelecionado;

	private String usernameView;
	private List<Publicacao> publicacoesByUsr;
	private Usuario usuarioView;

	private boolean buscarUsrBan;

	private Organizacao organizacao = new Organizacao();

	private List<Usuario> usuariosFiltrados;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	private String senhaOld;
	private String senhaNew;
	private String newEmail;

	private String emailUsuario;
	private boolean skipCamposCadastro;
	private static Logger logger = Logger.getLogger(Usuario.class.getName());

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{senhaMd5}")
	private SenhaMd5MB senhaMd5;

	@ManagedProperty(value = "#{emailMB}")
	private EmailMB emailMB;

	@ManagedProperty(value = "#{enderecoMB}")
	private EnderecoMB enderecoMB;

	@ManagedProperty(value = "#{arquivoMB}")
	private ArquivoMB arquivoMB;

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	@ManagedProperty(value = "#{atividadeUsuarioDao}")
	private AtividadeUsuarioDao atividadeUsuarioDao;

	@ManagedProperty(value = "#{atividadeUsuarioMB}")
	private AtividadeUsuarioMB atividadeUsuarioMB;

	private AtividadeUsuario atividadeUsuario = new AtividadeUsuario();

	@PostConstruct
	public void init() {
		usuarios = usuarioDao.listAll();
	}

	public void novo() {
		endereco = new Endereco();
		usuario = new Usuario();
	}

	public String remover() {
		usuarioDao.remover(usuario);
		usuarios = usuarioDao.listAll();
		return Paginas.PAGINA_USUARIOS;
	}

	public String cancelar() {
		usuario = null;
		return Paginas.PAGINA_USUARIOS;
	}

	public void loadPaginaUsr() {
		usuarioView = usuarioDao.findByLogin(usernameView);
		publicacoesByUsr = usuarioDao.findPblByUsr(usuarioView.getId());
	}

	public void buscarUsuariosBanidos() {
		if (buscarUsrBan) {
			usuariosFiltrados = usuarioDao.buscarUsuariosBanidos();

		} else {
			usuariosFiltrados = usuarioDao.listAll();

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

	public String login() throws UnsupportedEncodingException,
			NoSuchAlgorithmException, AuthenticationException,
			BadCredentialsException {
		System.out.println("logando usr");
		String pagina = "index";
		String senhaDigitada = senhaMd5.criptografar(usuario.getPassword());
		try {

			usuarioLogado = usuarioDao.findByLoginSenha(usuario.getUsername(),
					senhaDigitada);
			System.out.println("setando atividade usuario");

		} catch (NoResultException e) {
			usuario = null;
			pagina = "falhaLogin";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pagina;
	}

	public void atNovoUsuario() {
		if (getUsrLogado() != null) {
			System.out.println("nn e nulo o ususaoir o");
			usuarioLogado = getUsrLogado();
			atividadeUsuarioMB.salvatAT(usuarioLogado);
		}
	}

	public void banirUsuario() {
		usuario.setBanido(true);
		usuarioDao.update(usuario);
	}

	public String salvar() {
		if (usuario.getId() > 0) {
			usuario.getEndereco().setEstado(enderecoMB.getEstado());
			usuario.getEndereco().setCidade(enderecoMB.getCidade());
			System.out.println("setar avatar");
			usuario.setAvatar(arquivoMB.getArqSelecionado());
			usuarioDao.update(usuario);
		} else {
			usuario.setPassword(senhaMd5.criptografar(usuario.getPassword()));
			usuario.setEndereco(endereco);

			setarPreferencias();
			usuario.setPreferencias(preferencias);
			usuario.setAuthority("ROLE_USER");
			//
			if (arquivoMB.getArquivo() != null) {
				usuario.setAvatar(arquivoMB.getArquivo());
			} else {
				usuario.setAvatar(arquivoDao.findById(1));
			}

			usuarioDao.salvar(usuario);
			emailMB.enviarConfirmConta(usuario);
		}
		return "index";
	}

	public String informaNovoCpf() {
		return "doacoesOrg";
	}

	public void salvarNewSenha() {
		try {
			if (senhaMd5.criptografar(senhaOld).equals(usuario.getPassword())) {
				usuario.setPassword(senhaMd5.criptografar(senhaNew));
				usuarioDao.update(usuario);
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, null, "Pronto!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				FacesMessage msg = new FacesMessage(
						"O campo Senha Antiga está incorreto!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setarPreferencias() {
		preferencias.setContaConfirmada(false);
		preferencias.setCodValidaEmail(senhaMd5.criptografar(usuario.getEmail()
				+ usuario.getDataCadastro()));
	}

	public boolean validarLogin(FacesContext fc, UIComponent component,
			Object value) {
		try {
			if (usuario.getUsername() == null
					|| !usuario.getUsername().equals(value.toString())) {
				usuarioDao.findByLogin(value.toString());
				if (usuarioDao.findByLogin(value.toString()) == null) {
					return false;
				} else {
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Erro de validação",
							"Login ja cadastrado"));
				}
			} else {
				return false;
			}
		} catch (NoResultException e) {
		}
		return true;
	}

	public boolean validarEmail(FacesContext ctx, UIComponent component,
			Object value) throws ValidatorException {

		try {
			usuario.setEmail(value.toString());

			if (usuario.getEmail() == null
					|| !usuario.getEmail().equals(value.toString())
					|| validCaractEmail(value.toString())) {

				usuarioDao.findByEmail(value.toString());
				if (usuarioDao.findByEmail(value.toString()) == null) {
					return false;
				} else {
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Erro de validação",
							"E-mail ja cadastrado"));
				}
			} else {
				return false;
			}
		} catch (NoResultException e) {
		}
		return true;
	}

	public boolean validarEmailFace(FacesContext ctx, UIComponent component,
			Object value) throws ValidatorException {

		try {
			usuario.setEmail(value.toString());

			if (usuario.getEmail() == null
					|| !usuario.getEmail().equals(value.toString())
					|| validCaractEmail(value.toString())) {
				System.out.println("nesse if");
				usuarioDao.findByEmail(value.toString());
				if (usuario == usuarioDao.findByEmail(value.toString())) {
					System.out.println("o usuario Ž o ousuario oigual");
					return false;
				}
				if (usuarioDao.findByEmail(value.toString()) == null
						|| usuarioDao.findByEmail(value.toString()) == usuario) {
					return false;
				} else {
					throw new ValidatorException(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Erro de validação",
							"E-mail ja cadastrado"));
				}
			} else {
				return false;
			}
		} catch (NoResultException e) {
		}
		return true;
	}

	public boolean validCaractEmail(String email) {
		Pattern p = Pattern
				.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher m = p.matcher(email);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}

	public void updateEmail() {
		System.out.println("dando update no email");
		System.out.println("emil 1: " + usuario.getEmail());
		usuarioDao.update(usuario);
		System.out.println("emil 2: " + usuario.getEmail());
	}

	public boolean isSkip() {
		return skipCamposCadastro;
	}

	public void setSkip(boolean skip) {
		this.skipCamposCadastro = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		logger.info("Current wizard step:" + event.getOldStep());
		logger.info("Next step:" + event.getNewStep());

		if (skipCamposCadastro) {
			skipCamposCadastro = false;
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}

	public void fecharConta() {

		usuario = getUsrLogado();
		if (usuario.isDonoOrganizacao() == true) {
			organizacao = organizacaoDao.findOrgByMod(usuario.getId());
			organizacaoDao.remover(organizacao);
		}
		usuarioDao.remover(usuario);
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/Pegadas/j_spring_security_logout");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean validaCpf(FacesContext ctx, UIComponent component,
			Object event) throws ValidatorException {

		String ncpf = event.toString();

		String cpf = ncpf.replaceAll("-", "");

		String num = cpf.replace(".", "");

		cpf = num;
		System.out.println("cpf final: " + cpf);

		if (cpf.equals("11111111111") || cpf.equals("22222222222")
				|| cpf.equals("33333333333") || cpf.equals("44444444444")
				|| cpf.equals("55555555555") || cpf.equals("66666666666")
				|| cpf.equals("77777777777") || cpf.equals("88888888888")
				|| cpf.equals("99999999999")) {

			Integer primDig, segDig;
			int soma = 0, peso = 10;
			for (int i = 0; i < num.length(); i++)
				soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

			if (soma % 11 == 0 | soma % 11 == 1)
				primDig = new Integer(0);
			else
				primDig = new Integer(11 - (soma % 11));

			soma = 0;
			peso = 11;
			for (int i = 0; i < num.length(); i++)
				soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

			soma += primDig.intValue() * 2;
			if (soma % 11 == 0 | soma % 11 == 1)
				segDig = new Integer(0);
			else
				segDig = new Integer(11 - (soma % 11));

			primDig.toString();
			segDig.toString();
			System.out.println("DEU POsitiv A validacao");
			return true;
		} else {
			System.out.println("Validacao deu errado - cp invalido");
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro de validacao",
					"Valor nao reconhecido"));
		}

	}

	public void findUsrLogado() {
		usuario = getUsrLogado();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public List<Usuario> getUsuarios() {
		usuarios = usuarioDao.listAll();
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getSenhaOld() {
		return senhaOld;
	}

	public void setSenhaOld(String senhaOld) {
		this.senhaOld = senhaOld;
	}

	public String getSenhaNew() {
		return senhaNew;
	}

	public void setSenhaNew(String senhaNew) {
		this.senhaNew = senhaNew;
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

	public void setarFacebookTrue() {
		try {
			UsuarioController uc = new UsuarioController();

			uc.getUsuario().setFacebook(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String emailUsuarioLogado() {

		try {
			UsuarioController uc = new UsuarioController();

			String emailUsuario = uc.getUsuario().getEmail();
			usuario = usuarioDao.findByEmail(emailUsuario);
			emailUsuario = usuario.getEmail();

		} catch (Exception e) {
			emailUsuario = null;
		}
		return emailUsuario;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public boolean isSkipCamposCadastro() {
		return skipCamposCadastro;
	}

	public void setSkipCamposCadastro(boolean skipCamposCadastro) {
		this.skipCamposCadastro = skipCamposCadastro;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public Preferencias getPreferencias() {
		return preferencias;
	}

	public void setPreferencias(Preferencias preferencias) {
		this.preferencias = preferencias;
	}

	public EmailMB getEmailMB() {
		return emailMB;
	}

	public void setEmailMB(EmailMB emailMB) {
		this.emailMB = emailMB;
	}

	public String getUsernameView() {
		return usernameView;
	}

	public void setUsernameView(String usernameView) {
		this.usernameView = usernameView;
	}

	public String usuarios() {
		return Paginas.PAGINA_USUARIOS;
	}

	public String paginaUsuario() {
		return Paginas.PAGINA_USUARIO;
	}

	public String paginaCadastrarUsuario() {
		return Paginas.PAGINA_CADASTRO_USUARIO;
	}

	public String paginaEditarUsuario() {
		return Paginas.PAGINA_EDITAR_USUARIO;
	}

	public String paginaErro() {
		return Paginas.PAGINA_ERRO;
	}

	public String paginaCadastroFacebook() {
		return Paginas.PAGINA_AUTENTICACAO_LOGIN;
	}

	public String paginaConfUsr() {
		return Paginas.PAGINA_CONF_USR;
	}

	public List<Publicacao> getPublicacoesByUsr() {
		return publicacoesByUsr;
	}

	public void setPublicacoesByUsr(List<Publicacao> publicacoesByUsr) {
		this.publicacoesByUsr = publicacoesByUsr;
	}

	public EnderecoMB getEnderecoMB() {
		return enderecoMB;
	}

	public void setEnderecoMB(EnderecoMB enderecoMB) {
		this.enderecoMB = enderecoMB;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public Usuario getUsuarioView() {
		return usuarioView;
	}

	public void setUsuarioView(Usuario usuarioView) {
		this.usuarioView = usuarioView;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public ArquivoMB getArquivoMB() {
		return arquivoMB;
	}

	public void setArquivoMB(ArquivoMB arquivoMB) {
		this.arquivoMB = arquivoMB;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
	}

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
	}

	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public AtividadeUsuarioDao getAtividadeUsuarioDao() {
		return atividadeUsuarioDao;
	}

	public void setAtividadeUsuarioDao(AtividadeUsuarioDao atividadeUsuarioDao) {
		this.atividadeUsuarioDao = atividadeUsuarioDao;
	}

	public AtividadeUsuarioMB getAtividadeUsuarioMB() {
		return atividadeUsuarioMB;
	}

	public void setAtividadeUsuarioMB(AtividadeUsuarioMB atividadeUsuarioMB) {
		this.atividadeUsuarioMB = atividadeUsuarioMB;
	}

	public AtividadeUsuario getAtividadeUsuario() {
		return atividadeUsuario;
	}

	public void setAtividadeUsuario(AtividadeUsuario atividadeUsuario) {
		this.atividadeUsuario = atividadeUsuario;
	}

	public boolean isBuscarUsrBan() {
		return buscarUsrBan;
	}

	public void setBuscarUsrBan(boolean buscarUsrBan) {
		this.buscarUsrBan = buscarUsrBan;
	}

}
