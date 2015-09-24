package organizacao;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class OrganizacaoDataModel extends ListDataModel<Organizacao> implements
		SelectableDataModel<Organizacao> {

	public OrganizacaoDataModel() {
	}

	public OrganizacaoDataModel(List<Organizacao> data) {
		super(data);
	}

	@Override
	public Organizacao getRowData(String rowKey) {

		@SuppressWarnings("unchecked")
		List<Organizacao> orgs = (List<Organizacao>) getWrappedData();

		for (Organizacao org : orgs) {
			if (org.getNome().equals(rowKey))
				return org;
		}

		return null;
	}

	@Override
	public Object getRowKey(Organizacao org) {
		return org.getNome();
	}
}
