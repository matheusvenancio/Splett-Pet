package publicacao.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import publicacao.Publicacao;
import publicacao.dao.PublicacaoDao;
import publicacao.tipoPublicacao.TipoPublicacao;
import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import video.Video;
import video.dao.VideoDao;
import video.mb.VideoMB;
import animal.Animal;
import animal.cor.Cor;
import animal.dao.AnimalDao;
import animal.raca.Raca;
import animal.raca.dao.RacaDao;
import arquivo.Arquivo;
import arquivo.dao.ArquivoDao;
import arquivo.mb.ArquivoMB;
import email.mb.EmailMB;
import endereco.Endereco;
import endereco.cidade.Cidade;
import endereco.cidade.dao.CidadeDao;
import endereco.dao.EnderecoDao;
import endereco.mb.EnderecoMB;
import gmap.GMap;
import gmap.dao.GMapDao;
import gmap.mb.GMapMB;

@ManagedBean(name = "publicacaoMB")
@ViewScoped
public class PublicacaoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Publicacao> publicacoes;
	private List<Publicacao> publicacoesUsr;
	private int idPubliView;

	private Publicacao publicacao = new Publicacao();
	private Endereco endereco = new Endereco();
	private Animal animal = new Animal();
	private Usuario usuario = new Usuario();
	private Video video = new Video();
	private boolean isPerdido = false;
	private boolean isEncontrado = false;
	private boolean isAdocao = false;

	@ManagedProperty(value = "#{publicacaoDao}")
	private PublicacaoDao publicacaoDao;

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;

	@ManagedProperty(value = "#{cidadeDao}")
	private CidadeDao cidadeDao;

	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	@ManagedProperty(value = "#{racaDao}")
	private RacaDao racaDao;

	@ManagedProperty(value = "#{videoDao}")
	private VideoDao videoDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{arquivoDao}")
	private ArquivoDao arquivoDao;

	@ManagedProperty(value = "#{emailMB}")
	private EmailMB emailMB;

	@ManagedProperty(value = "#{gMapMB}")
	private GMapMB gMapMB;

	@ManagedProperty(value = "#{gMapDao}")
	private GMapDao gMapDao;

	@ManagedProperty(value = "#{enderecoMB}")
	private EnderecoMB enderecoMB;

	@ManagedProperty(value = "#{arquivoMB}")
	private ArquivoMB arquivoMB;

	@ManagedProperty(value = "#{videoMB}")
	private VideoMB videoMB;

	private List<Publicacao> publicacoesMatch = new ArrayList<Publicacao>();
	private List<Arquivo> imgs = new ArrayList<Arquivo>();
	private List<Video> videos = new ArrayList<Video>();

	private List<Animal> animaisVincula = new ArrayList<Animal>();
	private List<Raca> racasPorCategoria = new ArrayList<Raca>();
	private List<Cidade> cidadesByEstado = new ArrayList<Cidade>();

	private GMap gMap = new GMap();
	private boolean isGMap;

	private boolean isEdicaoAtiva = false;

	private boolean isCategoriaDifere = false;
	private boolean isIdadeEspec = false;

	@PostConstruct
	public void init() {
		publicacoes = publicacaoDao.listarPublicacoes();
	}

	public void findPubliById() {
		publicacao = publicacaoDao.findById(idPubliView);
		animal = publicacao.getAnimais().get(0);
	}

	public void obterCidadesPorEstado() {
		cidadesByEstado = cidadeDao.getCidadesByEstado(endereco.getEstado()
				.getId());
	}

	public void atualizarRacas() {
		gerarCampoOutros();
		racasPorCategoria = racaDao.getRacasByCategoria(animal.getCategoria()
				.getId());
	}

	public String paginaPublicacoes() {
		publicacoes = publicacaoDao.listarPublicacoes();
		return Paginas.PAGINA_PUBLICACOES;
	}

	public String paginaCadastroPublicacao() {
		return Paginas.PAGINA_CADASTRO_PUBLICACAO;
	}

	public void novo() {
		animal = new Animal();
		publicacao = new Publicacao();
		endereco = new Endereco();
	}

	public List<String> completeCores(String query) {
		List<String> match = new ArrayList<String>();
		Cor[] cores = Cor.values();
		for (int i = 0; i < cores.length; i++) {
			if (cores[i].getLabel().contains(query)) {
				match.add(cores[i].getLabel());
			}
		}
		return match;
	}

	public String remover() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"System Error", "Please try again later.");
		FacesContext.getCurrentInstance().addMessage(null, message);
		System.out.println("REMOVENDO PUBLICACAO");
		// animalDao.remover(animal);
		System.out.println("publicacao: " + publicacao.getTitulo());
		publicacaoDao.remover(publicacao);
		publicacoes = publicacaoDao.listAll();
		return "index";

	}

	public List<Publicacao> puliByUsr() {
		int id = findUsuarioLogado().getId();
		publicacoesUsr = publicacaoDao.publiByUsr(id);
		return publicacoesUsr;
	}

	public String cancelar() {
		publicacao = null;
		return Paginas.PAGINA_PUBLICACOES;
	}

	public void confirmarExclusao(ActionEvent actionEvent) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"System Error", "Please try again later.");

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String salvarPublicacao(GMap gMap) {
		if (publicacao.getIdPublicacao() > 0) {
			System.out.println("update publi");
			animalDao.update(animal);
			if (arquivoMB.getArqSelecionado() != null) {
				publicacao.setImgMain(arquivoMB.getArqSelecionado());
			}
			if (arquivoMB.getImgs() != null) {
				publicacao.setArquivos(arquivoMB.getImgs());
			}
			publicacaoDao.update(publicacao);

		} else {
			animal.setUsuario(findUsuarioLogado());
			animalDao.salvar(animal);
			animaisVincula.add(animal);
			endereco.setEstado(enderecoMB.getEstado());
			endereco.setCidade(enderecoMB.getCidade());
			enderecoDao.salvar(endereco);

			gMap.setEndereco(endereco);
			gMapDao.update(gMap);

			if (arquivoMB.getArqSelecionado() == null) {
				try {
					publicacao.setImgMain(arquivoMB.getImgs().get(0));
				} catch (Exception e) {
					publicacao.setImgMain(null);
				}
			} else {
				publicacao.setImgMain(arquivoMB.getArqSelecionado());
			}
			publicacao.setArquivos(arquivoMB.getImgs());
			publicacao.setEndereco(endereco);
			publicacao.setAnimais(animaisVincula);
			publicacao.setUsuario(findUsuarioLogado());

			System.out.println("titulo: " + publicacao.getTitulo());

			System.out.println("url: " + video.getId());

			System.out.println("setando video");
			// publicacao.setVideo(videoMB.getVideo());

			// publicacao.setVideos(videoMB.getVds());

			publicacaoDao.salvar(publicacao);

			for (int i = 0; i < videoMB.getVds().size(); i++) {
				video = videoMB.getVds().get(i);
				video.setAnimal(animal);
				System.out.println("VIDEO: " + video.getNome());
				videoDao.update(video);
			}
			if (usuario.getPreferencias().isEmailsAtividades()) {
				emailMB.enviarNovaPubli(publicacao.getUsuario());
			}
		}
		return "index";
	}

	public void adicionarVideo() {
		System.out.println("nome do video: " + video.getUrl());
		videoDao.salvar(video);
		videos.add(video);
		video = new Video();
	}

	public void match() {
		try {
			if (publicacao.getTipoPublicacao() != TipoPublicacao.ADOCAO) {
				publicacoesMatch = publicacaoDao.match(
						publicacao.getTipoPublicacao(), animal.getCategoria(),
						animal.getRaca(), animal.getSexo(),
						animal.getCorPrincipal(), animal.getDivisaoIdade(),
						enderecoMB.getEstado(), enderecoMB.getCidade());

				if (publicacao.getTipoPublicacao() == TipoPublicacao.ENCONTRADO) {
					isEncontrado = true;
					isPerdido = false;
					isAdocao = false;
					System.out.println("Ž encontrado: " + isEncontrado);
				} else {
					isPerdido = true;
					isAdocao = false;
					isEncontrado = false;
					System.out.println("Ž perdido: " + isPerdido);
				}
			} else {
				isAdocao = true;
				isPerdido = false;
				isEncontrado = false;
				System.out.println("Ž adocao: " + isAdocao);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public int publiUsrTam() {
		System.out.println("tamanho:" + publicacoesUsr.size());
		return publicacoesUsr.size();
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

	public void loadPaginaPubli() {
		findPubliById();
		usuario = findUsuarioLogado();
		imgs = arquivoDao.findArquivosByPubli(publicacao.getIdPublicacao());
	}

	public String paginaPubli() {
		return Paginas.PAGINA_PUBLICACAO;
	}

	public void gerarCampoOutros() {
		isCategoriaDifere = true;
	}

	public void gerarCampoIdade() {
		isIdadeEspec = true;
	}

	public void esconderCampoOutros() {
		isCategoriaDifere = false;
	}

	public void listenerEdicaoAtiva() {
		if (isEdicaoAtiva) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"Edição ativada: ",
									"clique nos campos que deseja alterar para acioná-los."));
		}
	}

	public List<Publicacao> getPublicacoes() {
		return publicacoes;
	}

	public void setPublicacoes(List<Publicacao> publicacoes) {
		this.publicacoes = publicacoes;
	}

	public int getIdPubliView() {
		return idPubliView;
	}

	public void setIdPubliView(int idPubliView) {
		this.idPubliView = idPubliView;
	}

	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public PublicacaoDao getPublicacaoDao() {
		return publicacaoDao;
	}

	public void setPublicacaoDao(PublicacaoDao publicacaoDao) {
		this.publicacaoDao = publicacaoDao;
	}

	public AnimalDao getAnimalDao() {
		return animalDao;
	}

	public void setAnimalDao(AnimalDao animalDao) {
		this.animalDao = animalDao;
	}

	public CidadeDao getCidadeDao() {
		return cidadeDao;
	}

	public void setCidadeDao(CidadeDao cidadeDao) {
		this.cidadeDao = cidadeDao;
	}

	public RacaDao getRacaDao() {
		return racaDao;
	}

	public void setRacaDao(RacaDao racaDao) {
		this.racaDao = racaDao;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public EmailMB getEmailMB() {
		return emailMB;
	}

	public void setEmailMB(EmailMB emailMB) {
		this.emailMB = emailMB;
	}

	public List<Animal> getAnimaisVincula() {
		return animaisVincula;
	}

	public void setAnimaisVincula(List<Animal> animaisVincula) {
		this.animaisVincula = animaisVincula;
	}

	public List<Raca> getRacasPorCategoria() {
		return racasPorCategoria;
	}

	public void setRacasPorCategoria(List<Raca> racasPorCategoria) {
		this.racasPorCategoria = racasPorCategoria;
	}

	public List<Cidade> getCidadesByEstado() {
		return cidadesByEstado;
	}

	public void setCidadesByEstado(List<Cidade> cidadesByEstado) {
		this.cidadesByEstado = cidadesByEstado;
	}

	public boolean isEdicaoAtiva() {
		return isEdicaoAtiva;
	}

	public void setEdicaoAtiva(boolean isEdicaoAtiva) {
		this.isEdicaoAtiva = isEdicaoAtiva;
	}

	public boolean isCategoriaDifere() {
		return isCategoriaDifere;
	}

	public void setCategoriaDifere(boolean isCategoriaDifere) {
		this.isCategoriaDifere = isCategoriaDifere;
	}

	public boolean isIdadeEspec() {
		return isIdadeEspec;
	}

	public void setIdadeEspec(boolean isIdadeEspec) {
		this.isIdadeEspec = isIdadeEspec;
	}

	public EnderecoMB getEnderecoMB() {
		return enderecoMB;
	}

	public void setEnderecoMB(EnderecoMB enderecoMB) {
		this.enderecoMB = enderecoMB;
	}

	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

	public List<Publicacao> getPublicacoesMatch() {
		return publicacoesMatch;
	}

	public void setPublicacoesMatch(List<Publicacao> publicacoesMatch) {
		this.publicacoesMatch = publicacoesMatch;
	}

	public ArquivoMB getArquivoMB() {
		return arquivoMB;
	}

	public void setArquivoMB(ArquivoMB arquivoMB) {
		this.arquivoMB = arquivoMB;
	}

	public List<Arquivo> getImgs() {
		return imgs;
	}

	public void setImgs(List<Arquivo> imgs) {
		this.imgs = imgs;
	}

	public ArquivoDao getArquivoDao() {
		return arquivoDao;
	}

	public void setArquivoDao(ArquivoDao arquivoDao) {
		this.arquivoDao = arquivoDao;
	}

	public boolean isGMap() {
		return isGMap;
	}

	public void setGMap(boolean isGMap) {
		this.isGMap = isGMap;
	}

	public boolean isPerdido() {
		return isPerdido;
	}

	public void setPerdido(boolean isPerdido) {
		this.isPerdido = isPerdido;
	}

	public boolean isEncontrado() {
		return isEncontrado;
	}

	public void setEncontrado(boolean isEncontrado) {
		this.isEncontrado = isEncontrado;
	}

	public boolean isAdocao() {
		return isAdocao;
	}

	public void setAdocao(boolean isAdocao) {
		this.isAdocao = isAdocao;
	}

	public List<Publicacao> getPublicacoesUsr() {
		return publicacoesUsr;
	}

	public void setPublicacoesUsr(List<Publicacao> publicacoesUsr) {
		this.publicacoesUsr = publicacoesUsr;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public VideoDao getVideoDao() {
		return videoDao;
	}

	public void setVideoDao(VideoDao videoDao) {
		this.videoDao = videoDao;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public GMapDao getgMapDao() {
		return gMapDao;
	}

	public void setgMapDao(GMapDao gMapDao) {
		this.gMapDao = gMapDao;
	}

	public GMapMB getgMapMB() {
		return gMapMB;
	}

	public void setgMapMB(GMapMB gMapMB) {
		this.gMapMB = gMapMB;
	}

	public GMap getgMap() {
		return gMap;
	}

	public void setgMap(GMap gMap) {
		this.gMap = gMap;
	}

	public VideoMB getVideoMB() {
		return videoMB;
	}

	public void setVideoMB(VideoMB videoMB) {
		this.videoMB = videoMB;
	}

}
