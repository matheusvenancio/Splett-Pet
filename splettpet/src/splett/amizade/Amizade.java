package splett.amizade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import splett.usuario.Usuario;

@Entity
@Table(name = "tbAmizade")
public class Amizade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JoinColumn(name="id_origem", referencedColumnName="id")
	@ManyToOne
	private Usuario usuarioOrigem;

	@JoinColumn(name="id_destino", referencedColumnName="id")
	@ManyToOne
	private Usuario usuarioDestino;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario1() {
		return usuarioOrigem;
	}

	public void setUsuario1(Usuario usuario1) {
		this.usuarioOrigem = usuario1;
	}

	public Usuario getUsuario2() {
		return usuarioDestino;
	}

	public void setUsuario2(Usuario usuario2) {
		this.usuarioDestino = usuario2;
	}
}
