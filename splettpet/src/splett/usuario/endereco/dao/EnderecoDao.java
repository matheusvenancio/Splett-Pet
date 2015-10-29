package splett.usuario.endereco.dao;

import java.util.List;

import splett.dao.Dao;
import splett.endereco.Logradouro;
import splett.usuario.endereco.Endereco;

public interface EnderecoDao extends Dao<Endereco>{
	
	public List<Logradouro> pesquisarPorCep(String cep);
	
}
