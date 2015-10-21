package splett.usuarioDisponibilidade.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import splett.dao.GenericDao;
import splett.usuarioDisponibilidade.UsuarioDisponibilidade;

@ManagedBean(name = "usuarioDisponibilidadeDao")
@ApplicationScoped
public class UsuarioDisponibilidadeDaoImpl extends GenericDao<UsuarioDisponibilidade>
	implements UsuarioDisponibilidadeDao {

    private static final long serialVersionUID = 1L;

    public UsuarioDisponibilidadeDaoImpl() {
	super(UsuarioDisponibilidade.class);
    }
}
