package splett.usuario.endereco.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import splett.usuario.endereco.Endereco;
import splett.usuario.endereco.dao.EnderecoDao;

@ManagedBean(name = "enderecoMB")
@ViewScoped
public class EnderecoMB {

	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	@ManagedProperty(value = "#{enderecoLazyDataModel}")
	private EnderecoLazyDataModel enderecoLazyDataModel;

	private List<Endereco> enderecoFiltered;

	private Endereco endereco;

	public EnderecoMB() {
		enderecoFiltered = new ArrayList<Endereco>();
	}

	public void criar() {
		endereco = new Endereco();
	}

	public void salvar() {
		if (endereco.getId() != null)
			enderecoDao.update(endereco);
		else
			enderecoDao.salvar(endereco);
	}

	public void remover() {
		enderecoDao.remover(endereco);
	}

	public void cancelar() {
		endereco = null;
	}

	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

	public EnderecoLazyDataModel getEnderecoLazyDataModel() {
		return enderecoLazyDataModel;
	}

	public void setEnderecoLazyDataModel(
			EnderecoLazyDataModel enderecoLazyDataModel) {
		this.enderecoLazyDataModel = enderecoLazyDataModel;
	}

	public List<Endereco> getEnderecoFiltered() {
		return enderecoFiltered;
	}

	public void setEnderecoFiltered(List<Endereco> enderecoFiltered) {
		this.enderecoFiltered = enderecoFiltered;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

}
