package publicacao;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import publicacao.tipoPublicacao.TipoPublicacao;
import usuario.Usuario;
import animal.Animal;
import arquivo.Arquivo;
import endereco.Endereco;

@Entity
@Table(name = "tbPublicacao")
@Inheritance(strategy = InheritanceType.JOINED)
public class Publicacao {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_publi")
	protected int idPublicacao;
	@Temporal(TemporalType.TIMESTAMP)
	protected Date dataCadastro = new java.sql.Date(System.currentTimeMillis());
	protected String titulo;

	@Enumerated(EnumType.STRING)
	protected TipoPublicacao tipoPublicacao;

	@ManyToMany
	@JoinTable(name = "publi_animais", joinColumns = { @JoinColumn(name = "id_publi") }, inverseJoinColumns = { @JoinColumn(name = "id_animal") })
	protected List<Animal> animais;
	
	protected boolean isFinalFeliz;

	@OneToOne
	protected Endereco endereco;

	@ManyToOne
	protected Usuario usuario;

	@OneToOne
	private Arquivo imgMain;

	
	@OneToMany
	protected List<Arquivo> arquivos;
	

	public TipoPublicacao getTipoPublicacao() {
		return tipoPublicacao;
	}

	public void setTipoPublicacao(TipoPublicacao tipoPublicacao) {
		this.tipoPublicacao = tipoPublicacao;
	}

	public int getIdPublicacao() {
		return idPublicacao;
	}

	public void setIdPublicacao(int idPublicacao) {
		this.idPublicacao = idPublicacao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Arquivo getImgMain() {
		return imgMain;
	}

	public void setImgMain(Arquivo imgMain) {
		this.imgMain = imgMain;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}



	public boolean isFinalFeliz() {
		return isFinalFeliz;
	}

	public void setFinalFeliz(boolean isFinalFeliz) {
		this.isFinalFeliz = isFinalFeliz;
	}

	
}
