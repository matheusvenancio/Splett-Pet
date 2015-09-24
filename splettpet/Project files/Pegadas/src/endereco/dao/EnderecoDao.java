package endereco.dao;

import java.util.List;

import dao.Dao;
import endereco.Endereco;

public interface EnderecoDao extends Dao<Endereco> {

	public List<Endereco> findEnderecoByOrg(int id);
}
