package arquivo.dao;

import java.util.List;

import arquivo.Arquivo;
import dao.Dao;

public interface ArquivoDao extends Dao<Arquivo> {

	public Arquivo findAvatarPadrao();
	public Arquivo findByNome(String nome);
	public List<Arquivo> findArquivosByPubli(Integer id);
	public List<Arquivo> findArquivosByMsg(int id);
}
