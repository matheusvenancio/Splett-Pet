package usuario.atividadeUsuario;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import usuario.Usuario;

@Entity
@Table(name = "tbAtividadeUsuario")
public class AtividadeUsuario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	private Usuario usuario;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataLogin;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataLogof;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getDataLogin() {
		return dataLogin;
	}
	public void setDataLogin(Date dataLogin) {
		this.dataLogin = dataLogin;
	}
	public Date getDataLogof() {
		return dataLogof;
	}
	public void setDataLogof(Date dataLogof) {
		this.dataLogof = dataLogof;
	}
	
	
}
