package usuario.sexo;

public enum SexoUsuario {
	FEMININO("Feminino"), MASCULINO("Masculino");

	private final String label;

	private SexoUsuario(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
