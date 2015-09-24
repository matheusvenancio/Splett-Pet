package organizacao.mb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import organizacao.Organizacao;
import organizacao.dao.OrganizacaoDao;

import usuario.dao.UsuarioDao;
import endereco.cidade.Cidade;
import endereco.dao.EnderecoDao;
import endereco.estado.Estado;

@ManagedBean(name = "pesquisaOrgMB")
@ViewScoped
public class PerquisaOrgMB {

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	private List<Organizacao> resultados;
	private Organizacao organizacao = new Organizacao();

	private boolean filtrarOrgsBanidas;

	private String nome;
	private Date dataCadastro;
	private String nomeMod;

	private Estado estado;
	private Cidade cidade;

	@PostConstruct
	public void init() {
		resultados = organizacaoDao.listAll();
	}

	public void pesquisarOrganizacoes() {
		if (estado == null) {
			System.out.println("estado nuLL");
			resultados = organizacaoDao.pesquisarOrganizacoes(nome,
					dataCadastro, nomeMod, null);
		} else {
			System.out.println("estado NAO nuLL");
			resultados = organizacaoDao.pesquisarOrganizacoes(nome,
					dataCadastro, nomeMod, estado.getNome());
		}
	}

	public void findOrgByNome() {
		organizacao = organizacaoDao.pesquisarOrgByNome(nome);
	}

	public void listarOrgsGerenciar() {
		System.out.println("listar orgs filtradas: " + filtrarOrgsBanidas);
		if (estado != null) {
			System.out.println("estado nao Ž nulo");
			resultados = organizacaoDao.listarOrgsGerenciar(estado.getNome(),
					filtrarOrgsBanidas);
		} else {
			System.out.println("estado Ž nulo");
			resultados = organizacaoDao
					.listarOrgsGerenciarPendentes(filtrarOrgsBanidas);
		}
	}
	
	
	

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
	}

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
	}

	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

	public List<Organizacao> getResultados() {
		return resultados;
	}

	public void setResultados(List<Organizacao> resultados) {
		this.resultados = resultados;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getNomeMod() {
		return nomeMod;
	}

	public void setNomeMod(String nomeMod) {
		this.nomeMod = nomeMod;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public boolean isFiltrarOrgsBanidas() {
		return filtrarOrgsBanidas;
	}

	public void setFiltrarOrgsBanidas(boolean filtrarOrgsBanidas) {
		this.filtrarOrgsBanidas = filtrarOrgsBanidas;
	}

}
