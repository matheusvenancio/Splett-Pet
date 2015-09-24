package doacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import organizacao.Organizacao;

import usuario.Usuario;
import doacao.banco.BancoDoacao;

@Entity
@Table(name = "tbDoacao")
public class Doacao {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 300)
	private float quantiaEmDinheiro;
	@Enumerated(EnumType.STRING)
	private BancoDoacao bancoDoacao;

	 @ManyToOne
	 @JoinColumn(name = "idUsuario")
	 private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "organizacao_id")
	private Organizacao organizacao;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getQuantiaEmDinheiro() {
		return quantiaEmDinheiro;
	}

	public void setQuantiaEmDinheiro(float quantiaEmDinheiro) {
		this.quantiaEmDinheiro = quantiaEmDinheiro;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BancoDoacao getBancoDoacao() {
		return bancoDoacao;
	}

	public void setBancoDoacao(BancoDoacao bancoDoacao) {
		this.bancoDoacao = bancoDoacao;
	}
	
	

	
}
