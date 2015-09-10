package splett.animal.raca.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.animal.raca.Raca;
import splett.animal.raca.dao.RacaDao;

@ManagedBean(name = "racaMB")
@ViewScoped
public class RacaMB {

	@ManagedProperty(value = "#{racaDao}")
	private RacaDao racaDao;

	@ManagedProperty(value = "#{racaLazyDataModel}")
	private RacaLazyDataModel racaLazyDataModel;

	private List<Raca> racasFiltered;

	private Raca raca;

	public RacaMB() {
		racasFiltered = new ArrayList<Raca>();
	}

	public void criar() {
		raca = new Raca();
	}

	public void salvar() {
		if (raca.getId() != null)
			racaDao.update(raca);
		else
			racaDao.salvar(raca);
	}

	public void remover() {
		racaDao.remover(raca);
	}

	public void cancelar() {
		raca = null;
	}

	public RacaDao getRacaDao() {
		return racaDao;
	}

	public void setRacaDao(RacaDao racaDao) {
		this.racaDao = racaDao;
	}

	public RacaLazyDataModel getRacaLazyDataModel() {
		return racaLazyDataModel;
	}

	public void setRacaLazyDataModel(RacaLazyDataModel racaLazyDataModel) {
		this.racaLazyDataModel = racaLazyDataModel;
	}

	public List<Raca> getRacasFiltered() {
		return racasFiltered;
	}

	public void setRacasFiltered(List<Raca> racasFiltered) {
		this.racasFiltered = racasFiltered;
	}

	public Raca getRaca() {
		return raca;
	}

	public void setRaca(Raca raca) {
		this.raca = raca;
	}
	
}
