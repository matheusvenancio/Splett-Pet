package doacao.banco;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "bancoDoacaoProvider")
@ViewScoped
public class BancoDoacaoProvider {

	public BancoDoacao[] getTipos() {
		return BancoDoacao.values();
	}
}
