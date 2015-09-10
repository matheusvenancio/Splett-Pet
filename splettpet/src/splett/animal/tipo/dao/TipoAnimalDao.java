package splett.animal.tipo.dao;

import java.util.List;

import splett.animal.tipo.TipoAnimal;
import splett.dao.Dao;

public interface TipoAnimalDao extends Dao<TipoAnimal> {

	public List<TipoAnimal> pesquisarPorNome(String nome);
}
