package animal;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import publicacao.Publicacao;
import usuario.Usuario;
import animal.categoria.Categoria;
import animal.divisaoIdade.DivisaoIdade;
import animal.porte.Porte;
import animal.raca.Raca;
import animal.sexo.Sexo;
import arquivo.Arquivo;

@Entity
@Table(name = "tbAnimal")
public class Animal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_animal")
	private int id;
	private String nome;
	private int idade;
	private boolean isCastrado;
	private boolean isVermifugado;
	@Column(length = 500)
	private String descri;
	@Enumerated(EnumType.STRING)
	private Sexo sexo;

	@Enumerated(EnumType.STRING)
	private DivisaoIdade divisaoIdade;

	private String corPrincipal;
	private String corSecundaria;

	@Enumerated(EnumType.STRING)
	private Porte porteAnimal;

	@Temporal(TemporalType.DATE)
	private Date dataCadastro = new java.sql.Date(System.currentTimeMillis());;

	@Temporal(TemporalType.DATE)
	private Date dataPE;


	@OneToMany
	protected List<Arquivo> imagens;
	
	@ManyToOne
	private Categoria categoria;

	@ManyToOne
	private Raca raca;

	@ManyToMany(mappedBy = "animais")
	private List<Publicacao> publicacoes;

	@OneToOne
	private Arquivo imgMain;

	@ManyToOne
	private Usuario usuario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public boolean isCastrado() {
		return isCastrado;
	}

	public void setCastrado(boolean isCastrado) {
		this.isCastrado = isCastrado;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public DivisaoIdade getDivisaoIdade() {
		return divisaoIdade;
	}

	public void setDivisaoIdade(DivisaoIdade divisaoIdade) {
		this.divisaoIdade = divisaoIdade;
	}

	public String getCorPrincipal() {
		return corPrincipal;
	}

	public void setCorPrincipal(String corPrincipal) {
		this.corPrincipal = corPrincipal;
	}

	public String getCorSecundaria() {
		return corSecundaria;
	}

	public void setCorSecundaria(String corSecundaria) {
		this.corSecundaria = corSecundaria;
	}

	public Porte getPorteAnimal() {
		return porteAnimal;
	}

	public void setPorteAnimal(Porte porteAnimal) {
		this.porteAnimal = porteAnimal;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataPE() {
		return dataPE;
	}

	public void setDataPE(Date dataPE) {
		this.dataPE = dataPE;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Raca getRaca() {
		return raca;
	}

	public void setRaca(Raca raca) {
		this.raca = raca;
	}

	public List<Publicacao> getPublicacoes() {
		return publicacoes;
	}

	public void setPublicacoes(List<Publicacao> publicacoes) {
		this.publicacoes = publicacoes;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isVermifugado() {
		return isVermifugado;
	}

	public void setVermifugado(boolean isVermifugado) {
		this.isVermifugado = isVermifugado;
	}

	

	public void setId(int id) {
		this.id = id;
	}

	public Arquivo getImgMain() {
		return imgMain;
	}

	public void setImgMain(Arquivo imgMain) {
		this.imgMain = imgMain;
	}

	public List<Arquivo> getImagens() {
		return imagens;
	}

	public void setImagens(List<Arquivo> imagens) {
		this.imagens = imagens;
	}

	

}
