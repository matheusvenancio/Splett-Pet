package animal.categoria.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import animal.categoria.Categoria;
import animal.categoria.dao.CategoriaDao;


@ManagedBean(name = "categoriaMB")
@ViewScoped
public class CategoriaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Categoria categoria = new Categoria();
	
	@ManagedProperty(value = "#{categoriaDao}")
	private CategoriaDao categoriaDao;

	private List<Categoria> categorias = new ArrayList<Categoria>();

	@PostConstruct
	public void init() {
		categorias = categoriaDao.listAll();
	}

	public void novo() {
		categoria = new Categoria();
	}

	public String salvar() {
		try {
			System.out.println("salvando cat: " + categoria.getNome());
			categoriaDao.salvar(categoria);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "categorias";
	}
	
	public String remover(){
		categoriaDao.remover(categoria);
		categorias = categoriaDao.listAll();
		return "categorias";
	}
	
	public void findCatById(Integer id){
		categoria = categoriaDao.findById(id);
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

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	
	

}
