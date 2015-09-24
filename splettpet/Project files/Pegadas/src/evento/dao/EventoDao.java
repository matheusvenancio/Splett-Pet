package evento.dao;

import java.util.List;

import animal.Animal;

import dao.Dao;
import evento.Evento;

public interface EventoDao extends Dao<Evento>{
	public List<Animal> listarAnimaisEvt(int id);
	
}
