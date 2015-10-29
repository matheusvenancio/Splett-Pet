package splett.solicitacao.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.amizade.Status;
import splett.animal.tipo.TipoAnimal;
import splett.animal.tipo.dao.TipoAnimalDao;

@ManagedBean(name = "solicitacaoTipoMB")
@ViewScoped
public class SolicitacaoTipoMB {

	@ManagedProperty(value = "#{tipoAnimalDao}")
	private TipoAnimalDao tipoAnimalDao;

	private TipoAnimal tipoAnimal;

	public void criar() {
		tipoAnimal = new TipoAnimal();
	}

	public void salvar() {
		tipoAnimal.setStatus(Status.ESPERA);
		tipoAnimalDao.salvar(tipoAnimal);
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

	public TipoAnimal getTipoAnimal() {
		return tipoAnimal;
	}

	public void setTipoAnimal(TipoAnimal tipoAnimal) {
		this.tipoAnimal = tipoAnimal;
	}
}
