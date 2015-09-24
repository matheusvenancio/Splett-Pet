package animal.categoria.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import animal.Animal;
import animal.categoria.Categoria;
import animal.categoria.dao.CategoriaDao;
import animal.dao.AnimalDao;

@ManagedBean(name = "categoriaAppMb")
@RequestScoped
public class CategoriaApplication {
//ignorar teste remover
	private List<Categoria> categorias;
	private Categoria categoria;

	@ManagedProperty(value = "#{categoriaDao}")
	private CategoriaDao categoriaDao;

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;
	
	private List<Animal> animaisVinculados;
	
	public List<Animal> salvarAnimaisCategoria(Categoria categoria, Animal animal) {
		animaisVinculados = new ArrayList<Animal>();
		animaisVinculados = animalDao.findAnimaisByCategoria(categoria.getId());
		animaisVinculados.add(animal);
		return animaisVinculados;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
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

	public AnimalDao getAnimalDao() {
		return animalDao;
	}

	public void setAnimalDao(AnimalDao animalDao) {
		this.animalDao = animalDao;
	}

}
