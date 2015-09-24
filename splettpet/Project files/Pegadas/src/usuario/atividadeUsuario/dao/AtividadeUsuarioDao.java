package usuario.atividadeUsuario.dao;

import usuario.atividadeUsuario.AtividadeUsuario;
import dao.Dao;

public interface AtividadeUsuarioDao extends Dao<AtividadeUsuario>{

	public AtividadeUsuario findAtividadeUsuarioById(int id);
}
