package publicacao.finalFeliz.dao;

import java.util.List;

import publicacao.finalFeliz.FinalFeliz;
import dao.Dao;

public interface FinalFelizDao extends Dao<FinalFeliz> {

	public List<FinalFeliz> listarFinaisFelizes();
}
