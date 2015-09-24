package organizacao.banco;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "bancoOrganizacaoProvider")
@ViewScoped
public class BancoOrganizacaoProvider {

	public BancoOrganizacao[] getTipos() {
		return BancoOrganizacao.values();
	}
}
