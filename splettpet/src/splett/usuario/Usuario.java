package splett.usuario;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import splett.amizade.Amizade;
import splett.animal.Animal;
import splett.avaliacao.Avaliacao;
import splett.foto.Foto;
import splett.genero.Genero;
import splett.mensagem.Mensagem;
import splett.postagem.Postagem;
import splett.usuario.endereco.Endereco;
import splett.video.Video;

@Entity
@Table(name = "tbUsuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "username", unique = true)
	private String email;

	@Column(name = "password")
	private String senha;

	private String nome;

	@Enumerated(EnumType.STRING)
	private Genero genero;

	private String telefoneCelular;

	private String telefoneFixo;

	private String fotoPerfil;

	@OneToOne
	private Endereco endereco;

	@Column(name = "authority")
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipo;

	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@OneToMany(mappedBy = "dono")
	private List<Animal> animais;

	@OneToMany
	@JoinColumn(name = "amigo_id", referencedColumnName = "id")
	private List<Amizade> amigos;

	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	@OneToMany
	private List<Postagem> postagens;

	/**
	 * TODO Many to Many
	 * 
	 * @OneToMany private List<Disponibilidade> disponibilidade;
	 */

	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	@OneToMany
	private List<Foto> fotos;

	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	@OneToMany
	private List<Video> videos;

	@JoinColumn(name = "usuario_avaliado_id", referencedColumnName = "id")
	@OneToMany
	private List<Avaliacao> avaliacoesRecebidas;

	@JoinColumn(name = "usuario_receptor_id", referencedColumnName = "id")
	@OneToMany
	private List<Mensagem> mensagensRecebidas;

	private boolean sexo_isPublico;

	private boolean telefoneFixo_isPublico;

	private boolean telefoneCelular_isPublico;

	private boolean dataNascimento_isPublico;

	private boolean email_isPublico;

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;

	}

	public List<Avaliacao> getAvaliacoesRecebidas() {
		return avaliacoesRecebidas;
	}

	public void setAvaliacoesRecebidas(List<Avaliacao> avaliacoes) {
		this.avaliacoesRecebidas = avaliacoes;
	}

	public List<Mensagem> getMensagensRecebidas() {
		return mensagensRecebidas;
	}

	public void setMensagensRecebidas(List<Mensagem> mensagens) {
		this.mensagensRecebidas = mensagens;
	}

	public boolean isSexo_isPublico() {
		return sexo_isPublico;
	}

	public void setSexo_isPublico(boolean sexo_isPublico) {
		this.sexo_isPublico = sexo_isPublico;
	}

	public boolean isTelefoneFixo_isPublico() {
		return telefoneFixo_isPublico;
	}

	public void setTelefoneFixo_isPublico(boolean telefone1_isPublico) {
		this.telefoneFixo_isPublico = telefone1_isPublico;
	}

	public boolean isTelefoneCelular_isPublico() {
		return telefoneCelular_isPublico;
	}

	public void setTelefoneCelular_isPublico(boolean telefone2_isPublico) {
		this.telefoneCelular_isPublico = telefone2_isPublico;
	}

	public boolean isDataNascimento_isPublico() {
		return dataNascimento_isPublico;
	}

	public void setDataNascimento_isPublico(boolean dataNascimento_isPublico) {
		this.dataNascimento_isPublico = dataNascimento_isPublico;
	}

	public boolean isEmail_isPublico() {
		return email_isPublico;
	}

	public void setEmail_isPublico(boolean email_isPublico) {
		this.email_isPublico = email_isPublico;
	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}

	public List<Postagem> getPostagens() {
		return postagens;
	}

	public void setPostagens(List<Postagem> postagens) {
		this.postagens = postagens;
	}

	public List<Foto> getFotos() {
		return fotos;
	}

	public void setFotos(List<Foto> fotos) {
		this.fotos = fotos;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPrimeiroNome() {
		return (nome.split(" "))[0];
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	public String getTelefoneFixo() {
		return telefoneFixo;
	}

	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}

	public boolean equals(Object other) {
		return other instanceof Usuario && (id != null) ? id
				.equals(((Usuario) other).getId()) : (other == this);
	}

	public int hashCode() {
		return id != null ? this.getClass().hashCode() + id.hashCode() : super
				.hashCode();
	}
}
