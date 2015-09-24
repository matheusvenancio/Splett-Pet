package animal.dao;

import java.util.List;

import animal.Animal;
import dao.Dao;

public interface AnimalDao extends Dao<Animal>{
	public List<Animal> findAnimaisByCategoria(Integer id);
}
