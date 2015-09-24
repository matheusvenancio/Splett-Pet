package banco;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import banco.tipo.TipoBanco;

import doacao.banco.BancoDoacao;

import organizacao.Organizacao;
import usuario.Usuario;

@Entity
@Table(name = "tbBanco")
public class Banco {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int numeroBoletoBB = 100;
	private int numeroBoletoItau = 100;
	private int numeroBoletoBradesco = 100;
	private String nome;
	private String path;
	private String nossoNumero;
	private int numCarteira;
	private String numeroAgencia;
	private String numeroConta;
	
	@ManyToOne
	private Organizacao organizacao;

	@ManyToOne
	private Usuario usuario;
	
	@Enumerated(EnumType.STRING)
	private TipoBanco tipoBanco;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro = new java.sql.Date(System.currentTimeMillis());

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumeroBoletoBB() {
		return numeroBoletoBB;
	}

	public void setNumeroBoletoBB(int numeroBoletoBB) {
		this.numeroBoletoBB = numeroBoletoBB;
	}

	public int getNumeroBoletoBradesco() {
		return numeroBoletoBradesco;
	}

	public void setNumeroBoletoBradesco(int numeroBoletoBradesco) {
		this.numeroBoletoBradesco = numeroBoletoBradesco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}



	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	

	public int getNumCarteira() {
		return numCarteira;
	}

	public void setNumCarteira(int numCarteira) {
		this.numCarteira = numCarteira;
	}

	public String getNumeroAgencia() {
		return numeroAgencia;
	}

	public void setNumeroAgencia(String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public String getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public TipoBanco getTipoBanco() {
		return tipoBanco;
	}

	public void setTipoBanco(TipoBanco tipoBanco) {
		this.tipoBanco = tipoBanco;
	}

	public int getNumeroBoletoItau() {
		return numeroBoletoItau;
	}

	public void setNumeroBoletoItau(int numeroBoletoItau) {
		this.numeroBoletoItau = numeroBoletoItau;
	}
	
	

	
	
	
}
