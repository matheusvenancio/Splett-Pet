package endereco.cidade.dao;

import java.util.List;

import dao.Dao;
import endereco.cidade.Cidade;

public interface CidadeDao extends Dao<Cidade>{

	public List<Cidade> getCidadesByEstado(Integer id);
}
