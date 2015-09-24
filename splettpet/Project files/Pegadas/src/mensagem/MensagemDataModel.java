package mensagem;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class MensagemDataModel extends ListDataModel<Mensagem> implements
		SelectableDataModel<Mensagem> {

	public MensagemDataModel() {
	}

	public MensagemDataModel(List<Mensagem> data) {
		super(data);
	}

	@Override
	public Mensagem getRowData(String rowKey) {

		@SuppressWarnings("unchecked")
		List<Mensagem> msgs = (List<Mensagem>) getWrappedData();

		for (Mensagem msg : msgs) {
			if (msg.getAssunto().equals(rowKey))
				return msg;
		}

		return null;
	}

	@Override
	public Object getRowKey(Mensagem msg) {
		return msg.getAssunto();
	}
}
