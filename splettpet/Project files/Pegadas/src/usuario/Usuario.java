package usuario;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.context.SecurityContextHolder;

import preferencias.Preferencias;
import usuario.sexo.SexoUsuario;
import arquivo.Arquivo;
import endereco.Endereco;

@Entity
@Table(name = "tbUsuarios")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 40)
	private String nome;
	@Column(length = 60)
	private String sobrenome;
	@Column(length = 15, unique = true)
	private String username;
	@Column(length = 35, unique = true)
	private String email;
	@Column(length = 80)
	private String password;

	private boolean isBanido;
	private boolean isAdmin;

	private String cpf;
	private String rg;

	@Enumerated(EnumType.STRING)
	private SexoUsuario sexoUsuario;

	private boolean isFacebook = false;
	private boolean isDonoOrganizacao = false;

	private String authority;

	@Column(length = 20)
	private String telefoneRes;
	@Column(length = 20)
	private String telefoneCel;

	@Column(length = 300)
	private String bio;

	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "idEndereco")
	private Endereco endereco;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro = new java.sql.Date(System.currentTimeMillis());

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Preferencias preferencias;

	@OneToOne
	private Arquivo avatar;

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getNome() {
		return nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTelefoneRes() {
		return telefoneRes;
	}

	public void setTelefoneRes(String telefoneRes) {
		this.telefoneRes = telefoneRes;
	}

	public String getTelefoneCel() {
		return telefoneCel;
	}

	public void setTelefoneCel(String telefoneCel) {
		this.telefoneCel = telefoneCel;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public boolean isModerador() {
		return authority.equals("ROLE_MOD");
	}

	public boolean isVoluntario() {
		return authority.equals("ROLE_VOLUNT");
	}

	public Usuario getUsuarioLogado() {
		Usuario user = (Usuario) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		return user;
	}

	public boolean isFacebook() {
		return isFacebook;
	}

	public void setFacebook(boolean isFacebook) {
		this.isFacebook = isFacebook;
	}

	public Preferencias getPreferencias() {
		return preferencias;
	}

	public void setPreferencias(Preferencias preferencias) {
		this.preferencias = preferencias;
	}

	public Arquivo getAvatar() {
		return avatar;
	}

	public void setAvatar(Arquivo avatar) {
		this.avatar = avatar;
	}

	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public SexoUsuario getSexoUsuario() {
		return sexoUsuario;
	}

	public void setSexoUsuario(SexoUsuario sexoUsuario) {
		this.sexoUsuario = sexoUsuario;
	}

	public boolean isDonoOrganizacao() {
		return isDonoOrganizacao;
	}

	public void setDonoOrganizacao(boolean isDonoOrganizacao) {
		this.isDonoOrganizacao = isDonoOrganizacao;
	}

	public boolean isBanido() {
		return isBanido;
	}

	public void setBanido(boolean isBanido) {
		this.isBanido = isBanido;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

}
