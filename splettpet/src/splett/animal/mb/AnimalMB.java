package splett.animal.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.animal.Animal;
import splett.animal.dao.AnimalDao;

@ManagedBean(name = "animalMB")
@ViewScoped
public class AnimalMB {

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;

	@ManagedProperty(value = "#{animalLazyDataModel}")
	private AnimalLazyDataModel animalLazyDataModel;

	private List<Animal> animaisFiltered;

	private Animal animal;

	public AnimalMB() {
		animaisFiltered = new ArrayList<Animal>();
	}

	public void criar() {
		animal = new Animal();
	}

	public void salvar() {
		if (animal.getId() != null)
			animalDao.update(animal);
		else
			animalDao.salvar(animal);
	}

	public void remover() {
		animalDao.remover(animal);
	}

	public AnimalLazyDataModel getAnimalLazyDataModel() {
		return animalLazyDataModel;
	}

	public void setAnimalLazyDataModel(AnimalLazyDataModel animalLazyDataModel) {
		this.animalLazyDataModel = animalLazyDataModel;
	}

	public void cancelar() {
		animal = null;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public List<Animal> getAnimaisFiltered() {
		return animaisFiltered;
	}

	public void setAnimaisFiltered(List<Animal> animaisFiltered) {
		this.animaisFiltered = animaisFiltered;
	}

	public AnimalDao getAnimalDao() {
		return animalDao;
	}

	public void setAnimalDao(AnimalDao animalDao) {
		this.animalDao = animalDao;
	}

}
