package publicacao.mb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import publicacao.Publicacao;
import publicacao.dao.PublicacaoDao;
import publicacao.tipoPublicacao.TipoPublicacao;
import animal.categoria.Categoria;
import animal.dao.AnimalDao;
import animal.divisaoIdade.DivisaoIdade;
import animal.porte.Porte;
import animal.raca.Raca;
import animal.sexo.Sexo;
import endereco.cidade.Cidade;
import endereco.estado.Estado;

@ManagedBean(name = "pesquisaPubliMB")
@ViewScoped
public class PesquisaPubliMB {

	@ManagedProperty(value = "#{publicacaoDao}")
	private PublicacaoDao publicacaoDao;

	@ManagedProperty(value = "#{animalDao}")
	private AnimalDao animalDao;

	private List<Publicacao> resultados;

	private String tf;
	private TipoPublicacao tipoPubli;
	private Categoria categoria;
	private Raca raca;
	private boolean isCastrado;
	private boolean isVermifugado;
	private Sexo sexo;
	private DivisaoIdade diviIdade;
	private String corPrincipal;
	private String corSecundaria;
	private Porte porte;
	private Date dataCadastro;
	private Date dataPE;

	private Estado estado;
	private Cidade cidade;

	@PostConstruct
	public void init() {
		resultados = publicacaoDao.listarPublicacoes();
	}

	public void pesquisarT() {
		System.out.println(tf);
		resultados = publicacaoDao.pesquisarT(tf, tipoPubli, categoria, estado);
	}

	public void pesquisar() {
		System.out.println("tf AAA: " + tf);
		resultados = publicacaoDao.pesquisar(tf, tipoPubli, categoria, raca,
				sexo, porte, corPrincipal, corSecundaria, diviIdade, estado);
	}

	public String paginaPesqAvancada() {
		resultados = null;
		return "pesquisarPubli";
	}

	public PublicacaoDao getPublicacaoDao() {
		return publicacaoDao;
	}

	public void setPublicacaoDao(PublicacaoDao publicacaoDao) {
		this.publicacaoDao = publicacaoDao;
	}

	public AnimalDao getAnimalDao() {
		return animalDao;
	}

	public void setAnimalDao(AnimalDao animalDao) {
		this.animalDao = animalDao;
	}

	public List<Publicacao> getResultados() {
		return resultados;
	}

	public void setResultados(List<Publicacao> resultados) {
		this.resultados = resultados;
	}

	public String getTf() {
		return tf;
	}

	public void setTf(String tf) {
		this.tf = tf;
	}

	public TipoPublicacao getTipoPubli() {
		return tipoPubli;
	}

	public void setTipoPubli(TipoPublicacao tipoPubli) {
		this.tipoPubli = tipoPubli;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
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

	public boolean isCastrado() {
		return isCastrado;
	}

	public void setCastrado(boolean isCastrado) {
		this.isCastrado = isCastrado;
	}

	public boolean isVermifugado() {
		return isVermifugado;
	}

	public void setVermifugado(boolean isVermifugado) {
		this.isVermifugado = isVermifugado;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public DivisaoIdade getDiviIdade() {
		return diviIdade;
	}

	public void setDiviIdade(DivisaoIdade diviIdade) {
		this.diviIdade = diviIdade;
	}

	public Porte getPorte() {
		return porte;
	}

	public void setPorte(Porte porte) {
		this.porte = porte;
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

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

}
