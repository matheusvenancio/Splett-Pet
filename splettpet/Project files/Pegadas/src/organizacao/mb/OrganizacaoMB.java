package organizacao.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import necessidade.Necessidade;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import banco.Banco;
import banco.BancoMB;
import banco.dao.BancoDao;
import organizacao.Organizacao;
import organizacao.dao.OrganizacaoDao;

import publicacao.Publicacao;
import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import usuario.mb.SenhaMd5MB;
import animal.Animal;
import animal.dao.AnimalDao;
import arquivo.Arquivo;
import arquivo.mb.ArquivoMB;
import doacao.mb.DoacaoMB;
import email.mb.EmailMB;
import endereco.Endereco;
import endereco.dao.EnderecoDao;
import endereco.mb.EnderecoMB;
import evento.Evento;

@ManagedBean(name = "organizacaoMB")
@ViewScoped
public class OrganizacaoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean isUsrAutorizado;
	private int idOrgView;

	private Organizacao organizacao = new Organizacao();
	private Animal animal = new Animal();
	private Usuario usuario = new Usuario();
	private Endereco endereco = new Endereco();
	private Evento evento = new Evento();
	private Banco banco;
	private List<Banco> bancos;

	private List<Organizacao> organizacoes;
	private List<Endereco> enderecos = new ArrayList<Endereco>();
	private List<Necessidade> necessidades;
	private List<Animal> animais;
	private List<Evento> eventos;

	private boolean isEditar;
	private boolean isGerVolunt;
	private boolean isAddVolunt;
	private String emailAddVolunt;

	private String codUsrConvite;
	private int idOrgConvite;

	private String nome;
	private String nossoNum;

	private boolean isBB;
	private boolean isBradesco;
	private boolean isItau;
	private boolean isItauC;

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{enderecoMB}")
	private EnderecoMB enderecoMB;

	@ManagedProperty(value = "#{arquivoMB}")
	private ArquivoMB arquivoMB;

	@ManagedProperty(value = "#{emailMB}")
	private EmailMB emailMB;

	@ManagedProperty(value = "#{senhaMd5}")
	private SenhaMd5MB senhaMd5;

	@ManagedProperty(value = "#{bancoMB}")
	private BancoMB bancoMB;

	@ManagedProperty(value = "#{bancoDao}")
	private BancoDao bancoDao;

	private List<Arquivo> imgsView;
	private List<Usuario> participantesView;
	private List<Publicacao> atividadesView;
	private List<Endereco> enderecosView;

	@PostConstruct
	public void init() {
		organizacoes = organizacaoDao.listAll();
	}

	public void loadGerenciarOrgs() {
		organizacoes = organizacaoDao.findOrgsPendentes();
		organizacoes.addAll(organizacaoDao.findOrgs());
	}

	public void loadOrganizacao() {
		findOrgById();
		imgsView = new ArrayList<Arquivo>(organizacao.getArquivos());
		imgsView.add(organizacao.getImgMain());
		participantesView = new ArrayList<Usuario>(organizacao.getVoluntarios());
		participantesView.add(organizacao.getModerador());
		atividadesView = new ArrayList<Publicacao>(organizacao.getId());
		enderecosView = new ArrayList<Endereco>(
				enderecoDao.findEnderecoByOrg(organizacao.getId()));
		enderecos = new ArrayList<Endereco>(
				enderecoDao.findEnderecoByOrg(organizacao.getId()));
	}

	public void loadOrganizacoes() {
		organizacoes = organizacaoDao.findOrgs();
	}

	public void loadConvite() {
		usuario = usuarioDao.findByCod(codUsrConvite);
		organizacao = organizacaoDao.findById(idOrgConvite);
	}

	public void findOrgByNecessidade(Necessidade necessidade) {
		organizacao = organizacaoDao.findOrgByNecessidade(necessidade);
	}

	public void loadGerenciarOrg() {
		usuario = findUsrLogado();
		System.out.println("load org: " + usuario.getNome());
		try {
			System.out.println("find usr: " + usuario.getNome());
			organizacao = organizacaoDao.findOrgByMod(usuario.getId());
			System.out.println("nome org: " + organizacao.getNome());
			enderecosView = organizacao.getEnderecos();
		} catch (NoResultException e) {
			try {
				organizacao = organizacaoDao.findOrgByVol(usuario.getId());
			} catch (NoResultException n) {
				return;
			}
		}
	}

	public void aprovar() {
		if (!organizacao.isConfirmado()) {
			usuario = organizacao.getModerador();
			if (usuario.getAuthority() == "ROLE_ADMIN"
					|| usuario.getAuthority().equals("ROLE_ADMIN")) {
				usuario.setAuthority("ROLE_ADMIN");
			} else {
				usuario.setAuthority("ROLE_MOD");
			}
			usuarioDao.update(usuario);
			organizacao.setConfirmado(true);
			organizacaoDao.update(organizacao);

			FacesMessage msg = new FacesMessage("A organização "
					+ organizacao.getNome() + " foi aprovada.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			organizacao.setConfirmado(false);
			organizacaoDao.update(organizacao);

			FacesMessage msg = new FacesMessage("A organização "
					+ organizacao.getNome() + " está agora pendente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void autorizarOrg() {
		try {
			if (organizacao.getId() > 0) {
				organizacaoDao.update(organizacao);
				FacesMessage msg = new FacesMessage("Organização Autorizada!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(
					"Não foi possível completar a requisição!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void buscarOrgUsuario(int id) {
		organizacao = organizacaoDao.buscarOrgUsuario(id);
	}

	public void findOrgById() {
		organizacao = organizacaoDao.findById(idOrgView);
	}

	public void novo() {
		organizacao = new Organizacao();
		endereco = new Endereco();
	}

	public void isBancoItau() {
		setItauC(true);
	}

	public void nBancoItauC() {
		setItauC(false);
	}

	public String remover() {
		System.out.println("deletando org....");
		usuario = findUsrLogado();
		usuario.setDonoOrganizacao(false);
		organizacaoDao.remover(organizacao);
		organizacoes = organizacaoDao.listAll();
		return "index";
	}

	public void verificaBB(int id) {
		if (organizacaoDao.verificarExisteBB(id) != null) {
			System.out.println("Ž banco do brasil");
			setBB(true);
		} else {
			System.out.println("n‹o Ž banco do brasil");
			setBB(false);
		}
	}

	public void verificaBradesco(int id) {
		if (organizacaoDao.verificarExisteBradesco(id) != null) {
			System.out.println("Ž banco bradesco");
			setBradesco(true);
		} else {
			System.out.println("n‹o Ž banco bradesco");
			setBradesco(false);
		}
	}

	public void verificaItau(int id) {
		if (organizacaoDao.verificarExisteItau(id) != null) {
			System.out.println("Ž banco itau");
			setItau(true);
		} else {
			System.out.println("n‹o Ž banco itau");
			setItau(false);
		}
	}

	public String cancelar() {
		organizacao = null;
		return Paginas.PAGINA_ORGANIZACOES;
	}

	public void addVoluntario() {
		try {
			emailMB.enviarConviteVolt(usuarioDao.findByEmail(emailAddVolunt),
					organizacao);
		} catch (NullPointerException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					null, "Erro! Usuario nao encontrado.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			System.out.println("Erro!");
		}
	}

	public String salvar() {
		if (organizacao.getId() > 0) {
			if (arquivoMB.getArqSelecionado() != null) {
				System.out.println("update a");
				organizacao.setImgMain(arquivoMB.getArqSelecionado());
				organizacao.setArquivos(arquivoMB.getImgs());
			}
			organizacaoDao.update(organizacao);
		} else {
			organizacao.setModerador(findUsrLogado());
			// List<Endereco> enderecosSalvar = new ArrayList<Endereco>();
			// enderecosSalvar.add(endereco);
			// TODO:

			try {
				endereco.setEstado(enderecoMB.getEstado());
				endereco.setCidade(enderecoMB.getCidade());
				enderecos.add(endereco);
				organizacao.setEnderecos(enderecos);
			} catch (Exception e) {
				System.out.println("setar end org erro");
				e.printStackTrace();
			}

			if (arquivoMB.getArqSelecionado() == null) {
				try {
					organizacao.setImgMain(arquivoMB.getImgs().get(0));
				} catch (Exception e) {
					organizacao.setImgMain(null);
				}
			} else {
				organizacao.setImgMain(arquivoMB.getArqSelecionado());
			}

			organizacao.setArquivos(arquivoMB.getImgs());
			organizacao.setAnimais(animais);
			organizacaoDao.salvar(organizacao);
			usuario = findUsrLogado();
			System.out.println("nome do usuario logado: " + findUsrLogado().getNome());
			usuario.setDonoOrganizacao(true);
			usuarioDao.update(usuario);

			System.out.println("for BancO");
			System.out.println("tamanho selecionados: "
					+ bancoMB.getBancosSelecionados().size());
			for (int i = 0; i < bancoMB.getBancosSelecionados().size(); i++) {
				System.out.println("i: " + i);
				banco = bancoMB.getBancosSelecionados().get(i);
				banco.setOrganizacao(organizacao);
				System.out.println("BANCO: " + banco.getNome());
				bancoDao.update(banco);
			}
		}
		return "index";
	}

	public List<Banco> listarBancosOrg(int id) {
		bancos = bancoDao.listarBancosOrg(id);
		return bancos;
	}

	public void salvarNovoEndereco() {
		endereco.setEstado(enderecoMB.getEstado());
		endereco.setCidade(enderecoMB.getCidade());
		organizacao.getEnderecos().add(endereco);
		organizacaoDao.update(organizacao);
	}

	public String salvarAnimal() {
		if (animal.getId() > 0) {
			animalDao.update(animal);
		} else {
			animalDao.salvar(animal);
			animais.add(animal);
			organizacao.setAnimais(animais);
		}
		return Paginas.PAGINA_ANIMAIS;
	}

	public String doarParaOrganizacao(int id) {
		organizacao = organizacaoDao.findById(id);
		DoacaoMB doacaoMB = new DoacaoMB();
		return doacaoMB.paginaCadastroDoacao(id);
	}

	public String aceitarConvite() {
		List<Usuario> voluntarios = organizacao.getVoluntarios();
		voluntarios.add(usuario);
		if (usuario.getAuthority() == "ROLE_ADMIN") {
			usuario.setAuthority("ROLE_ADMIN");
		} else {
			usuario.setAuthority("ROLE_VOLUNT");
		}
		usuario.setDonoOrganizacao(true);
		usuarioDao.update(usuario);
		organizacao.setVoluntarios(voluntarios);
		organizacaoDao.update(organizacao);
		return "gerOrg";
	}

	public void cadastroLoad() {
		usuario = findUsrLogado();
		System.out.println("Nome usr p org: " + usuario.getNome());
		if (usuario.getEndereco().getEstado() == null
				|| usuario.getEndereco().getCidade() == null
				|| usuario.getTelefoneRes() == null
				|| usuario.getTelefoneCel() == null) {
			isUsrAutorizado = false;
		} else {
			enderecoMB.setEstado(usuario.getEndereco().getEstado());
			enderecoMB.setCidade(usuario.getEndereco().getCidade());
			isUsrAutorizado = true;
		}
	}

	public Evento ultimoEvtCadastrado(int id) {
		System.out.println("id pa: " + id);
		evento = organizacaoDao.ultimoEventoTermino(id);
		System.out.println("eveno nome: " + evento.getTitulo());
		return evento;
	}

	public void gerenciarVoltar() {
		isEditar = false;
		isGerVolunt = false;
		isAddVolunt = false;
	}

	public Usuario findUsrLogado() {
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

	public List<Evento> listarEvtsOrg(int id) {
		eventos = organizacaoDao.listEventoByOrg(id);
		return eventos;
	}

	public List<Evento> listarUltimosEvtsOrg(int id) {
		eventos = organizacaoDao.listUltimosEventoByOrg(id);
		return eventos;
	}

	public List<Necessidade> listarNecessidadesOrg(int id) {
		necessidades = organizacaoDao.listNecessidadesByOrg(id);
		return necessidades;
	}

	public List<Animal> listarAnimaisOrg(int id) {
		animais = organizacaoDao.listAnimaisByOrg(id);
		return animais;
	}

	public String paginaOrganizacoes() {
		organizacoes = organizacaoDao.listAll();
		return Paginas.PAGINA_ORGANIZACOES;
	}

	public String paginaOrganizacao() {
		return Paginas.PAGINA_ORGANIZACAO;
	}

	public String paginaCadastroOrganizacao() {
		return Paginas.PAGINA_CADASTRO_ORGANIZACAO;
	}

	public boolean isUsrAutorizado() {
		return isUsrAutorizado;
	}

	public void setUsrAutorizado(boolean isUsrAutorizado) {
		this.isUsrAutorizado = isUsrAutorizado;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public List<Organizacao> getOrganizacoes() {
		return organizacoes;
	}

	public void setOrganizacoes(List<Organizacao> organizacoes) {
		this.organizacoes = organizacoes;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
	}

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getIdOrgView() {
		return idOrgView;
	}

	public void setIdOrgView(int idOrgView) {
		this.idOrgView = idOrgView;
	}

	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public EnderecoMB getEnderecoMB() {
		return enderecoMB;
	}

	public void setEnderecoMB(EnderecoMB enderecoMB) {
		this.enderecoMB = enderecoMB;
	}

	public ArquivoMB getArquivoMB() {
		return arquivoMB;
	}

	public void setArquivoMB(ArquivoMB arquivoMB) {
		this.arquivoMB = arquivoMB;
	}

	public List<Arquivo> getImgsView() {
		return imgsView;
	}

	public void setImgsView(List<Arquivo> imgsView) {
		this.imgsView = imgsView;
	}

	public List<Usuario> getParticipantesView() {
		return participantesView;
	}

	public void setParticipantesView(List<Usuario> participantesView) {
		this.participantesView = participantesView;
	}

	public List<Publicacao> getAtividadesView() {
		return atividadesView;
	}

	public void setAtividadesView(List<Publicacao> atividadesView) {
		this.atividadesView = atividadesView;
	}

	public List<Endereco> getEnderecosView() {
		return enderecosView;
	}

	public void setEnderecosView(List<Endereco> enderecosView) {
		this.enderecosView = enderecosView;
	}

	public boolean isEditar() {
		return isEditar;
	}

	public void setEditar(boolean isEditar) {
		this.isEditar = isEditar;
	}

	public boolean isGerVolunt() {
		return isGerVolunt;
	}

	public void setGerVolunt(boolean isGerVolunt) {
		this.isGerVolunt = isGerVolunt;
	}

	public boolean isAddVolunt() {
		return isAddVolunt;
	}

	public void setAddVolunt(boolean isAddVolunt) {
		this.isAddVolunt = isAddVolunt;
	}

	public String getEmailAddVolunt() {
		return emailAddVolunt;
	}

	public void setEmailAddVolunt(String emailAddVolunt) {
		this.emailAddVolunt = emailAddVolunt;
	}

	public EmailMB getEmailMB() {
		return emailMB;
	}

	public void setEmailMB(EmailMB emailMB) {
		this.emailMB = emailMB;
	}

	public int getIdOrgConvite() {
		return idOrgConvite;
	}

	public void setIdOrgConvite(int idOrgConvite) {
		this.idOrgConvite = idOrgConvite;
	}

	public String getCodUsrConvite() {
		return codUsrConvite;
	}

	public void setCodUsrConvite(String codUsrConvite) {
		this.codUsrConvite = codUsrConvite;
	}

	public SenhaMd5MB getSenhaMd5() {
		return senhaMd5;
	}

	public void setSenhaMd5(SenhaMd5MB senhaMd5) {
		this.senhaMd5 = senhaMd5;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public List<Necessidade> getNecessidades() {
		return necessidades;
	}

	public void setNecessidades(List<Necessidade> necessidades) {
		this.necessidades = necessidades;
	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public AnimalDao getAnimalDao() {
		return animalDao;
	}

	public void setAnimalDao(AnimalDao animalDao) {
		this.animalDao = animalDao;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public BancoMB getBancoMB() {
		return bancoMB;
	}

	public void setBancoMB(BancoMB bancoMB) {
		this.bancoMB = bancoMB;
	}

	public BancoDao getBancoDao() {
		return bancoDao;
	}

	public void setBancoDao(BancoDao bancoDao) {
		this.bancoDao = bancoDao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNossoNum() {
		return nossoNum;
	}

	public void setNossoNum(String nossoNum) {
		this.nossoNum = nossoNum;
	}

	public boolean isBB() {
		return isBB;
	}

	public void setBB(boolean isBB) {
		this.isBB = isBB;
	}

	public boolean isBradesco() {
		return isBradesco;
	}

	public void setBradesco(boolean isBradesco) {
		this.isBradesco = isBradesco;
	}

	public boolean isItau() {
		return isItau;
	}

	public void setItau(boolean isItau) {
		this.isItau = isItau;
	}

	public boolean isItauC() {
		return isItauC;
	}

	public void setItauC(boolean isItauC) {
		this.isItauC = isItauC;
	}

}
