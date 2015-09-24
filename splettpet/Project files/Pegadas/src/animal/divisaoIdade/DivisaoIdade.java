package animal.divisaoIdade;

public enum DivisaoIdade {
	FILHOTE("Filhote"), ADULTO("Adulto");

	private final String label;

	private DivisaoIdade(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
