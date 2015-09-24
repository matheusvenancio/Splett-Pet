package animal.porte;

public enum Porte {
	PEQUENO("Pequeno"), MEDIO("Medio"), GRANDE("Grande");

	private final String label;

	private Porte(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
