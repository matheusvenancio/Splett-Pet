package animal.raca.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import publicacao.mb.PublicacaoMB;
import sistema.Paginas;
import animal.categoria.Categoria;
import animal.categoria.dao.CategoriaDao;
import animal.mb.AnimalMB;
import animal.raca.Raca;
import animal.raca.dao.RacaDao;

@ManagedBean(name = "racaMB")
@ViewScoped
public class RacaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Raca> racas;
	private Raca raca = new Raca();

	private Categoria categoriaSelecionada;

	private Categoria categoria;

	@ManagedProperty(value = "#{categoriaDao}")
	private CategoriaDao categoriaDao;

	@ManagedProperty(value = "#{racaDao}")
	private RacaDao racaDao;

	@ManagedProperty(value = "#{publicacaoMB}")
	private PublicacaoMB publicacaoMB;

	@ManagedProperty(value = "#{animalMB}")
	private AnimalMB animalMB;

	public void findRacasByCategoria() {
		try {
			racas = racaDao.getRacasByCategoria(publicacaoMB.getAnimal()
					.getCategoria().getId());
			publicacaoMB.setCategoriaDifere(false);
		} catch (Exception e) {
			racas = racaDao.getRacasByCategoria(animalMB.getAnimal()
					.getCategoria().getId());
			animalMB.setCategoriaDifere(false);
		}
	}

	@PostConstruct
	public void init() {
		racas = racaDao.listAll();
	}

	public void novo() {
		raca = new Raca();
	}

	public String remover() {
		racaDao.remover(raca);
		racas = racaDao.listAll();
		return "racas";
	}

	public String salvar() {
		if (raca.getId() > 0) {
			racaDao.update(raca);
		} else {
			racaDao.salvar(raca);
		}
		return Paginas.PAGINA_GERENCIAR_CATEGORIAS;
	}

	public List<Raca> listarRacasByCategoria(Integer id) {
		return racaDao.listarRacasByCategoria(id);
	}

	public String salvarRacaByCat(Integer id){
		raca.setCategoria(categoriaDao.findCategoriaById(id));
		racaDao.salvar(raca);
		return "racas";
	}

	public Categoria getCategoriaSelecionada() {
		return categoriaSelecionada;
	}

	public void setCategoriaSelecionada(Categoria categoriaSelecionada) {
		this.categoriaSelecionada = categoriaSelecionada;
	}

	public List<Raca> getRacas() {
		return racas;
	}

	public void setRacas(List<Raca> racas) {
		this.racas = racas;
	}

	public Raca getRaca() {
		return raca;
	}

	public void setRaca(Raca raca) {
		this.raca = raca;
	}

	public RacaDao getRacaDao() {
		return racaDao;
	}

	public void setRacaDao(RacaDao racaDao) {
		this.racaDao = racaDao;
	}

	public PublicacaoMB getPublicacaoMB() {
		return publicacaoMB;
	}

	public void setPublicacaoMB(PublicacaoMB publicacaoMB) {
		this.publicacaoMB = publicacaoMB;
	}

	public AnimalMB getAnimalMB() {
		return animalMB;
	}

	public void setAnimalMB(AnimalMB animalMB) {
		this.animalMB = animalMB;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public CategoriaDao getCategoriaDao() {
		return categoriaDao;
	}

	public void setCategoriaDao(CategoriaDao categoriaDao) {
		this.categoriaDao = categoriaDao;
	}
	
	

}
