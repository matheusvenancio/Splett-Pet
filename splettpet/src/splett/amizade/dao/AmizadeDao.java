package splett.amizade.dao;

import java.util.List;

import splett.amizade.Amizade;
import splett.dao.Dao;
import splett.usuario.Usuario;

public interface AmizadeDao extends Dao<Amizade> {

    public List<Usuario> listAmigos(Usuario usuario);
    
    public List<Usuario> listarAmigosOrigem(Usuario usuario);
}
