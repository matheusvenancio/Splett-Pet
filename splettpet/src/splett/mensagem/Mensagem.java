package splett.mensagem;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import splett.usuario.Usuario;

@Entity
@Table(name = "tbMensagem")
public class Mensagem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String texto;

	@Temporal(TemporalType.DATE)
	private Date data;

	@Temporal(TemporalType.DATE)
	private Date hora;

	@JoinColumn(referencedColumnName = "id", name = "usuario_emissor_id")
	@ManyToOne
	private Usuario emissor;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public Usuario getEmissor() {
		return emissor;
	}

	public void setEmissor(Usuario emissor) {
		this.emissor = emissor;
	}

}
