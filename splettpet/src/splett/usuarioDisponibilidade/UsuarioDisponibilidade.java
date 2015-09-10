package splett.usuarioDisponibilidade;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import splett.disponibilidade.Disponibilidade;
import splett.usuario.Usuario;

public class UsuarioDisponibilidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JoinColumn(referencedColumnName = "id", name = "usuario_id")
	@ManyToOne
	private Usuario usuario;

	@JoinColumn(referencedColumnName = "id", name = "disponibilidade_id")
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
