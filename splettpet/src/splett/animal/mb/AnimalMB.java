package splett.animal.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.animal.Animal;
import splett.animal.dao.AnimalDao;
import splett.animal.raca.Raca;
import splett.animal.tipo.TipoAnimal;
import splett.animal.tipo.dao.TipoAnimalDao;
import splett.perfil.mb.PerfilMB;

@ManagedBean(name = "animalMB")
@ViewScoped
public class AnimalMB {

    @ManagedProperty(value = "#{animalDao}")
    private AnimalDao animalDao;

    @ManagedProperty(value = "#{tipoAnimalDao}")
    private TipoAnimalDao tipoAnimalDao;

    @ManagedProperty(value = "#{animalLazyDataModel}")
    private AnimalLazyDataModel animalLazyDataModel;

    @ManagedProperty(value = "#{perfilMB}")
    private PerfilMB perfilMB;

    private List<Animal> animaisFiltered;

    private List<Animal> animaisUsuarioVisualizado;

    private Animal animal;

    private List<Raca> racas;

    public AnimalMB() {
	animaisFiltered = new ArrayList<Animal>();
    }

    public void criar() {
	racas = new ArrayList<Raca>();
	animal = new Animal();
	Raca r = new Raca();
	r.setTipoAnimal(new TipoAnimal());
	animal.setRaca(r);
    }

    public void salvar() {
	if (animal.getDono() == null)
	    animal.setDono(perfilMB.getUsuarioVisualizado());
	if (animal.getId() != null)
	    animalDao.update(animal);
	else
	    animalDao.salvar(animal);
    }

    public void listarRacas() {
	racas = tipoAnimalDao.listRacas(this.animal.getRaca().getTipoAnimal().getId());
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

    public TipoAnimalDao getTipoAnimalDao() {
	return tipoAnimalDao;
    }

    public void setTipoAnimalDao(TipoAnimalDao tipoAnimalDao) {
	this.tipoAnimalDao = tipoAnimalDao;
    }

    public List<Raca> getRacas() {
	return racas;
    }

    public void setRacas(List<Raca> racas) {
	this.racas = racas;
    }

    public List<Animal> getAnimaisUsuarioVisualizado() {
	animaisUsuarioVisualizado = animalDao.getAnimaisUsuario(perfilMB.getUsuarioVisualizado());
	return animaisUsuarioVisualizado;
    }

    public void setAnimaisUsuarioVisualizado(List<Animal> animaisUsuarioVisualizado) {
	this.animaisUsuarioVisualizado = animaisUsuarioVisualizado;
    }

    public PerfilMB getPerfilMB() {
	return perfilMB;
    }

    public void setPerfilMB(PerfilMB perfilMB) {
	this.perfilMB = perfilMB;
    }

}
