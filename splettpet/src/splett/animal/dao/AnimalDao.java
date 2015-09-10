package splett.animal.dao;

import java.util.List;

import splett.animal.Animal;
import splett.dao.Dao;

public interface AnimalDao extends Dao<Animal> {

	public List<Animal> pesquisarPorNome(String nome);
}
