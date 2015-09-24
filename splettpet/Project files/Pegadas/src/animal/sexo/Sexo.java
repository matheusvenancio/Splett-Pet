package animal.sexo;

public enum Sexo {

	MACHO("Macho"), FEMEA("Femea"), NA("Não conhecido");

	private final String label;

	private Sexo(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
