package splett.usuario.dao;

import java.util.List;

import splett.dao.Dao;
import splett.usuario.Usuario;

public interface UsuarioDao extends Dao<Usuario> {

	public List<Usuario> pesquisarPorNome(String nome);

	public Usuario pesquisarPorEmail(String email);
}
