package publicacao.tipoPublicacao;

public enum TipoPublicacao {
	ENCONTRADO("Encontrado"), ADOCAO("Adocao"), PERDIDO("Perdido");

	private final String label;

	private TipoPublicacao(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		switch (this) {
		case ENCONTRADO: {
			return "ENCONTRADO";
		}
		case ADOCAO: {
			return "ADOCAO";
		}
		case PERDIDO: {
			return "PERDIDO";
		}
		}
		return null;
	}
}
