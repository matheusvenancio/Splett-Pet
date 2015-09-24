package banco.tipo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "tipoBancoProvider")
@ViewScoped
public class TipoBancoProvider {

	public TipoBanco[] getTipos() {
		return TipoBanco.values();
	}
}
