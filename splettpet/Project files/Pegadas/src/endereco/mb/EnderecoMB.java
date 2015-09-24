package endereco.mb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import endereco.cidade.Cidade;
import endereco.cidade.dao.CidadeDao;
import endereco.estado.Estado;
import endereco.estado.dao.EstadoDao;

@ManagedBean(name = "enderecoMB")
@ViewScoped
public class EnderecoMB {

	private Cidade cidade;
	private Estado estado;

	private List<Estado> estados;
	private List<Cidade> cidades;

	@ManagedProperty(value = "#{estadoDao}")
	private EstadoDao estadoDao;

	@ManagedProperty(value = "#{cidadeDao}")
	private CidadeDao cidadeDao;

	@PostConstruct
	public void init() {
		estados = estadoDao.listAll();
	}

	public void findCidadesByEstado() {
		cidades = cidadeDao.getCidadesByEstado(estado.getId());
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public EstadoDao getEstadoDao() {
		return estadoDao;
	}

	public void setEstadoDao(EstadoDao estadoDao) {
		this.estadoDao = estadoDao;
	}

	public CidadeDao getCidadeDao() {
		return cidadeDao;
	}

	public void setCidadeDao(CidadeDao cidadeDao) {
		this.cidadeDao = cidadeDao;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

}
