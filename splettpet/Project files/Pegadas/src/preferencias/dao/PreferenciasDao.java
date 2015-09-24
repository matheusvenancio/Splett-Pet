package preferencias.dao;

import preferencias.Preferencias;
import usuario.Usuario;
import dao.Dao;

public interface PreferenciasDao extends Dao<Preferencias> {

	public Usuario getUsuarioByCod(String cod);
}
