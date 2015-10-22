package splett.usuario.endereco.mb;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

import splett.usuario.endereco.Endereco;

public class LazyEnderecoSorter implements Comparator<Endereco> {

	private String sortField;

	private SortOrder sortOrder;

	public LazyEnderecoSorter(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Endereco u1, Endereco u2) {
		try {
			Object value1 = Endereco.class.getField(this.sortField).get(u1);
			Object value2 = Endereco.class.getField(this.sortField).get(u2);

			int value = ((Comparable) value1).compareTo(value2);

			return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}