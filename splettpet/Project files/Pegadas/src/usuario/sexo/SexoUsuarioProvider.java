package usuario.sexo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "sexoUsuarioProvider")
@ViewScoped
public class SexoUsuarioProvider {

	public SexoUsuario[] getTipos() {
		return SexoUsuario.values();
	}
}
