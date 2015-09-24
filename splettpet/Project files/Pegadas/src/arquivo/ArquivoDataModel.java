package arquivo;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class ArquivoDataModel extends ListDataModel<Arquivo> implements
		SelectableDataModel<Arquivo> {


	public ArquivoDataModel() {
	}

	public ArquivoDataModel(List<Arquivo> data) {
		super(data);
	}

	@Override
	public Arquivo getRowData(String rowKey) {
		@SuppressWarnings("unchecked")
		List<Arquivo> arqs = (List<Arquivo>) getWrappedData();

		for (Arquivo arq : arqs) {
			if (arq.getNome().equals(rowKey))
				return arq;
		}

		return null;
	}

	@Override
	public Object getRowKey(Arquivo arq) {
		return arq.getNome();
	}


}