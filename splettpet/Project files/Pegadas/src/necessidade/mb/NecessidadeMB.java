package necessidade.mb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.DualListModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import organizacao.Organizacao;
import organizacao.dao.OrganizacaoDao;
import organizacao.mb.OrganizacaoMB;

import animal.Animal;
import animal.dao.AnimalDao;

import necessidade.Necessidade;
import necessidade.dao.NecessidadeDao;
import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;

@ManagedBean(name = "necessidadeMB")
@ViewScoped
public class NecessidadeMB {

	private List<Necessidade> necessidades;
	private Necessidade necessidade = new Necessidade();
	private Animal animal = new Animal();
	private Organizacao organizacao = new Organizacao();
	private Usuario usuario = new Usuario();

	private boolean possuiAnimal;
	private boolean adicionaAnimal;

	private boolean isCategoriaDifere = false;
	private boolean isIdadeEspec = false;
	int nescView;

	private List<Animal> animais;
	private DualListModel<Animal> animaisNecessidade;

	@ManagedProperty(value = "#{necessidadeDao}")
	private NecessidadeDao necessidadeDao;

	@ManagedProperty(value = "#{organizacaoMB}")
	private OrganizacaoMB organizacaoMB;

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	@PostConstruct
	public void init() {
		necessidades = necessidadeDao.listAll();

	}

	public String paginaNecessidades() {
		necessidades = necessidadeDao.listAll();
		return Paginas.PAGINA_NECESSIDADES;
	}

	public String paginaCadastroNecessiade() {
		return Paginas.PAGINA_CADASTRO_NECESSIDADE;
	}

	public void novo() {
		necessidade = new Necessidade();
		animal = new Animal();
	}

	public String remover() {
		necessidadeDao.remover(necessidade);
		necessidades = necessidadeDao.listAll();
		return Paginas.PAGINA_NECESSIDADES;
	}

	public String cancelar() {
		necessidade = null;
		return Paginas.PAGINA_NECESSIDADES;
	}

	public void listarAnimaisNecessidade(int id){
		animais = necessidadeDao.listarAnimaisNecessidade(id);
	}
	
	public void carregarOrgNecessidades() {
		List<Animal> animaisTarget = new ArrayList<Animal>();
		try {
			organizacao = organizacaoDao.findOrgByMod(findUsrLogado().getId());
		} catch (Exception e) {
			organizacao = organizacaoDao.findOrgByVol(findUsrLogado().getId());
		}
		System.out.println("carregando org necessidade: "
				+ organizacao.getNome());
		animais = organizacaoMB.listarAnimaisOrg(organizacao.getId());

		animaisNecessidade = new DualListModel<Animal>(animais, animaisTarget);

	}

	public String salvar() {
		try {
			organizacao = organizacaoDao.findOrgByMod(findUsrLogado().getId());
		} catch (Exception e) {
			organizacao = organizacaoDao.findOrgByVol(findUsrLogado().getId());
		}
		if (necessidade.getIdPublicacao() > 0) {
			necessidadeDao.update(necessidade);
		} else {

			necessidade.setUsuario(findUsrLogado());
			necessidade.setOrganizacao(organizacao);

			necessidade.setAnimais(animaisNecessidade.getTarget());
			necessidadeDao.salvar(necessidade);
		}

		return "gerOrg";
	}

	public void findNecessidadeById(int id){
		necessidade = necessidadeDao.findById(id);
		System.out.println("necessidade: " + necessidade.getIdPublicacao());
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
	
	public void listarPrincipaisNecessidades(){
		necessidades = necessidadeDao.listarPrincipaisNecessidades();
	}

	public List<Necessidade> getNecessidades() {
		return necessidades;
	}

	public void setNecessidades(List<Necessidade> necessidades) {
		this.necessidades = necessidades;
	}

	public Necessidade getNecessidade() {
		return necessidade;
	}

	public void setNecessidade(Necessidade necessidade) {
		this.necessidade = necessidade;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public boolean isPossuiAnimal() {
		return possuiAnimal;
	}

	public void setPossuiAnimal(boolean possuiAnimal) {
		this.possuiAnimal = possuiAnimal;
	}

	public NecessidadeDao getNecessidadeDao() {
		return necessidadeDao;
	}

	public void setNecessidadeDao(NecessidadeDao necessidadeDao) {
		this.necessidadeDao = necessidadeDao;
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

	public AnimalDao getAnimalDao() {
		return animalDao;
	}

	public void setAnimalDao(AnimalDao animalDao) {
		this.animalDao = animalDao;
	}

	public boolean isAdicionaAnimal() {
		return adicionaAnimal;
	}

	public void setAdicionaAnimal(boolean adicionaAnimal) {
		this.adicionaAnimal = adicionaAnimal;
	}

	public void gerarCampoOutros() {
		isCategoriaDifere = true;
	}

	public void gerarCampoIdade() {
		isIdadeEspec = true;
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

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
	}

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
	}

	public DualListModel<Animal> getAnimaisNecessidade() {
		return animaisNecessidade;
	}

	public void setAnimaisNecessidade(DualListModel<Animal> animaisNecessidade) {
		this.animaisNecessidade = animaisNecessidade;
	}

	public OrganizacaoMB getOrganizacaoMB() {
		return organizacaoMB;
	}

	public void setOrganizacaoMB(OrganizacaoMB organizacaoMB) {
		this.organizacaoMB = organizacaoMB;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public int getNescView() {
		return nescView;
	}

	public void setNescView(int nescView) {
		this.nescView = nescView;
	}

	
}
