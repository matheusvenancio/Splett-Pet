package splett.usuarioDisponibilidade;

import javax.persistence.ManyToOne;

import splett.disponibilidade.Disponibilidade;
import splett.usuario.Usuario;

public class UsuarioDisponibilidade {

	@ManyToOne
	private Usuario usuario;

	@ManyToOne
	private Disponibilidade disponibilidade;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Disponibilidade getDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(Disponibilidade disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

}
