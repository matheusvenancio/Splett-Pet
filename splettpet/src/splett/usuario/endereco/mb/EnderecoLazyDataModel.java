package splett.usuario.endereco.mb;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import splett.usuario.endereco.Endereco;
import splett.usuario.endereco.dao.EnderecoDao;

@ManagedBean(name = "enderecoLazyDataModel")
@ViewScoped
public class EnderecoLazyDataModel extends LazyDataModel<Endereco> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	@Override
	public Endereco getRowData(String rowKey) {
		return enderecoDao.findById(Integer.parseInt(rowKey));
	}

	@Override
	public Object getRowKey(Endereco endereco) {
		return endereco.getId();
	}

	@Override
	public List<Endereco> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<Endereco> source = null;

		if (filters.containsKey("nome")) {
			String nomePesquisa = filters.get("nome").toString();
			source = enderecoDao.pesquisarPorCidade(nomePesquisa);
		} else {
			source = enderecoDao.list(first, pageSize);
		}

		// sort
		if (sortField != null) {
			Collections.sort(source,
					new LazyEnderecoSorter(sortField, sortOrder));
		}

		// rowCount
		this.setRowCount(enderecoDao.getRowCount());

		return source;
	}

	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

}
