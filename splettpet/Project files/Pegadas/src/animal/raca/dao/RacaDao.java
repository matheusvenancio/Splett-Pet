package animal.raca.dao;

import java.util.List;

import animal.raca.Raca;
import dao.Dao;

public interface RacaDao extends Dao<Raca>{
	public List<Raca> getRacasByCategoria(Integer id);
	
	public List<Raca> listarRacasByCategoria(Integer id);
}
