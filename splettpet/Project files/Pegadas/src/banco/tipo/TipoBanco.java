package banco.tipo;

public enum TipoBanco {
	BANCOBRASIL("BancoBrasil"), BRADESCO("Bradesco"), ITAU("Itau");

	private final String label;

	private TipoBanco(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
