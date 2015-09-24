package organizacao;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import banco.Banco;

import usuario.Usuario;
import animal.Animal;
import arquivo.Arquivo;
import doacao.banco.BancoDoacao;
import endereco.Endereco;

@Entity
@Table(name = "tbOrganizacao")
public class Organizacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nome;
	@Column(length = 400)
	private String descricao;
	@Column(length = 400)
	private String proposta;
	private String telefone;
	private boolean isConfirmado;

	@Enumerated(EnumType.STRING)
	private BancoDoacao bancoOrganizacao;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Banco> bancos;


	private boolean isBancoBrasil = false;
	private boolean isBancoBradesco = false;
	private boolean isBancoItau = false;
	private String cnpj;

	@OneToOne
	private Usuario moderador;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Usuario> voluntarios;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Animal> animais;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Endereco> enderecos;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro = new java.sql.Date(System.currentTimeMillis());

	@OneToOne
	private Arquivo imgMain;

	@OneToMany
	private List<Arquivo> arquivos;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getProposta() {
		return proposta;
	}

	public void setProposta(String proposta) {
		this.proposta = proposta;
	}

	public List<Usuario> getVoluntarios() {
		return voluntarios;
	}

	public void setVoluntarios(List<Usuario> voluntarios) {
		this.voluntarios = voluntarios;
	}

	

	public boolean isConfirmado() {
		return isConfirmado;
	}

	public void setConfirmado(boolean isConfirmado) {
		this.isConfirmado = isConfirmado;
	}

	

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Usuario getModerador() {
		return moderador;
	}

	public void setModerador(Usuario moderador) {
		this.moderador = moderador;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Arquivo getImgMain() {
		return imgMain;
	}

	public void setImgMain(Arquivo imgMain) {
		this.imgMain = imgMain;
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public boolean isBancoBrasil() {
		return isBancoBrasil;
	}

	public void setBancoBrasil(boolean isBancoBrasil) {
		this.isBancoBrasil = isBancoBrasil;
	}

	public boolean isBancoBradesco() {
		return isBancoBradesco;
	}

	public void setBancoBradesco(boolean isBancoBradesco) {
		this.isBancoBradesco = isBancoBradesco;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public BancoDoacao getBancoOrganizacao() {
		return bancoOrganizacao;
	}

	public void setBancoOrganizacao(BancoDoacao bancoOrganizacao) {
		this.bancoOrganizacao = bancoOrganizacao;
	}

	
	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}


	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public boolean isBancoItau() {
		return isBancoItau;
	}

	public void setBancoItau(boolean isBancoItau) {
		this.isBancoItau = isBancoItau;
	}
	
	

}
