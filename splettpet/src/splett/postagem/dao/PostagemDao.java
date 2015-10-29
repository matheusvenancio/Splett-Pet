package splett.postagem.dao;

import java.util.List;

import splett.dao.Dao;
import splett.postagem.Postagem;

public interface PostagemDao extends Dao<Postagem>{
	
    public List<Postagem> listarPostagens(Integer id);

}
