package doacao.banco;

public enum BancoDoacao {
	BANCOBRASIL("BancoBrasil"), BRADESCO("Bradesco"), ITAU("Itau");

	private final String label;

	private BancoDoacao(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
