package animal.cor;

public enum Cor {
	CARAMELO("Caramelo"), VERDE("Verde"), MARROM("Marrom"), VERMELHO("Vermelho");

	private final String label;

	private Cor(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
