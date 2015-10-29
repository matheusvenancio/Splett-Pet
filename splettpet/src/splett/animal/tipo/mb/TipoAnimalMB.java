package splett.animal.tipo.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.amizade.Status;
import splett.animal.tipo.TipoAnimal;
import splett.animal.tipo.dao.TipoAnimalDao;

@ManagedBean(name = "tipoAnimalMB")
@ViewScoped
public class TipoAnimalMB {

	@ManagedProperty(value = "#{tipoAnimalDao}")
	private TipoAnimalDao tipoAnimalDao;

	@ManagedProperty(value = "#{tipoAnimalLazyDataModel}")
	private TipoAnimalLazyDataModel tipoAnimalLazyDataModel;

	@ManagedProperty(value = "#{solicitacaoTipoAnimalLazyDataModel}")
	private SolicitacaoTipoAnimalLazyDataModel solicitacaoTipoAnimalLazyDataModel;

	private List<TipoAnimal> tipoAnimalFiltered;

	private TipoAnimal tipoAnimal;

	public TipoAnimalMB() {
		tipoAnimalFiltered = new ArrayList<TipoAnimal>();
	}

	public void criar() {
		tipoAnimal = new TipoAnimal();
	}

	public void salvar() {
		if (tipoAnimal.getId() != null)
			tipoAnimalDao.update(tipoAnimal);
		else
			tipoAnimalDao.salvar(tipoAnimal);
	}

	public void aceitar() {
		tipoAnimal.setStatus(Status.ACEITO);

		tipoAnimalDao.update(tipoAnimal);
	}

	public SolicitacaoTipoAnimalLazyDataModel getSolicitacaoTipoAnimalLazyDataModel() {
		return solicitacaoTipoAnimalLazyDataModel;
	}

	public void setSolicitacaoTipoAnimalLazyDataModel(
			SolicitacaoTipoAnimalLazyDataModel solicitacaoTipoAnimalLazyDataModel) {
		this.solicitacaoTipoAnimalLazyDataModel = solicitacaoTipoAnimalLazyDataModel;
	}

	public void remover() {
		tipoAnimalDao.remover(tipoAnimal);
	}

	public void cancelar() {
		tipoAnimal = null;
	}

	public TipoAnimalDao getTipoAnimalDao() {
		return tipoAnimalDao;
	}

	public void setTipoAnimalDao(TipoAnimalDao tipoAnimalDao) {
		this.tipoAnimalDao = tipoAnimalDao;
	}

	public TipoAnimalLazyDataModel getTipoAnimalLazyDataModel() {
		return tipoAnimalLazyDataModel;
	}

	public void setTipoAnimalLazyDataModel(
			TipoAnimalLazyDataModel tipoAnimalLazyDataModel) {
		this.tipoAnimalLazyDataModel = tipoAnimalLazyDataModel;
	}

	public List<TipoAnimal> getTipoAnimalFiltered() {
		return tipoAnimalFiltered;
	}

	public void setTipoAnimalFiltered(List<TipoAnimal> tipoAnimalFiltered) {
		this.tipoAnimalFiltered = tipoAnimalFiltered;
	}

	public TipoAnimal getTipoAnimal() {
		return tipoAnimal;
	}

	public void setTipoAnimal(TipoAnimal tipoAnimal) {
		this.tipoAnimal = tipoAnimal;
	}

}
