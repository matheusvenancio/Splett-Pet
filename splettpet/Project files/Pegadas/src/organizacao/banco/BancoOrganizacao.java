package organizacao.banco;

public enum BancoOrganizacao {
	BANCOBRASIL("BancoBrasil"), BRADESCO("Bradesco"), ITAU("Itau");

	private final String label;

	private BancoOrganizacao(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
