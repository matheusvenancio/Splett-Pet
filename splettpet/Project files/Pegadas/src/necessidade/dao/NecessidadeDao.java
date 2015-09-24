package necessidade.dao;

import java.util.List;

import animal.Animal;

import necessidade.Necessidade;
import dao.Dao;

public interface NecessidadeDao extends Dao<Necessidade> {
	
	public Necessidade findNecessidadeById(int id);
	
	public List<Animal> listarAnimaisNecessidade(int id);
	
	public List<Necessidade> listarPrincipaisNecessidades();
}
