package animal.categoria.dao;

import animal.categoria.Categoria;
import dao.Dao;

public interface CategoriaDao extends Dao<Categoria> {
	public Categoria findByNome(String nome);

	public Categoria findCategoriaById(Integer id);
}
