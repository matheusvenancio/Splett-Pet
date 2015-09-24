package animal.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import endereco.Endereco;

import organizacao.Organizacao;
import organizacao.dao.OrganizacaoDao;

import animal.Animal;
import animal.cor.Cor;
import animal.dao.AnimalDao;
import animal.raca.Raca;
import animal.raca.dao.RacaDao;
import arquivo.mb.ArquivoMB;
import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;

@ManagedBean(name = "animalMB")
@ViewScoped
public class AnimalMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private Animal animal = new Animal();

	private List<Animal> animais;
	private Endereco endereco = new Endereco();

	private Usuario usuario = new Usuario();

	@ManagedProperty(value = "#{arquivoMB}")
	private ArquivoMB arquivoMB;

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{racaDao}")
	private RacaDao racaDao;

	private Organizacao organizacao = new Organizacao();

	private List<Raca> racasPorCategoria = new ArrayList<Raca>();

	private boolean isCategoriaDifere = false;
	private boolean isIdadeEspec = false;

	@PostConstruct
	public void init() {
		animais = animalDao.listAll();
	}

	public String paginaAnimais() {
		animais = animalDao.listAll();
		return Paginas.PAGINA_ANIMAIS;
	}

	public String paginaListaAnimais() {
		return Paginas.PAGINA_LISTA_ANIMAIS;
	}

	public String paginaCadastroAnimal() {
		return Paginas.PAGINA_CADASTRO_ANIMAL;
	}

	public void novo() {
		animal = new Animal();
	}

	public String remover() {
		animalDao.remover(animal);
		animais = animalDao.listAll();
		return Paginas.PAGINA_ANIMAIS;
	}

	public String cancelar() {
		animal = null;
		return Paginas.PAGINA_ANIMAIS;
	}

	public String salvar() {
		try {
			organizacao = organizacaoDao.findOrgByMod(findUsrLogado().getId());
		} catch (Exception e) {
			organizacao = organizacaoDao.findOrgByVol(findUsrLogado().getId());
		}
		if (animal.getId() > 0) {
			animalDao.update(animal);
		} else {
			try {
				animal.setUsuario(findUsrLogado());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (arquivoMB.getArqSelecionado() == null) {
				try {
					animal.setImgMain(arquivoMB.getImgs().get(0));
				} catch (Exception e) {
					animal.setImgMain(null);
				}
			} else {
				animal.setImgMain(arquivoMB.getArqSelecionado());
			}
			animal.setImagens(arquivoMB.getImgs());

			animalDao.salvar(animal);
			organizacao.getAnimais().add(animal);
			organizacaoDao.update(organizacao);
		}
		return "gerOrganizacao";
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

	public void buscarAnimalPorId(int id) {
		animal = animalDao.findById(id);
	}

	public void atualizarRacas() {
		gerarCampoOutros();
		racasPorCategoria = racaDao.getRacasByCategoria(animal.getCategoria()
				.getId());
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

	public Boolean EAnimalAdocao() {
		return Boolean.TRUE;
	}

	public Boolean NaoEAnimalAdocao() {
		return Boolean.FALSE;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
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

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public RacaDao getRacaDao() {
		return racaDao;
	}

	public void setRacaDao(RacaDao racaDao) {
		this.racaDao = racaDao;
	}

	public List<Raca> getRacasPorCategoria() {
		return racasPorCategoria;
	}

	public void setRacasPorCategoria(List<Raca> racasPorCategoria) {
		this.racasPorCategoria = racasPorCategoria;
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

	public ArquivoMB getArquivoMB() {
		return arquivoMB;
	}

	public void setArquivoMB(ArquivoMB arquivoMB) {
		this.arquivoMB = arquivoMB;
	}

}
