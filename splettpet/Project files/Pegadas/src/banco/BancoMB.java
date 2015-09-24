package banco;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import necessidade.Necessidade;
import necessidade.dao.NecessidadeDao;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import organizacao.Organizacao;
import organizacao.boleto.mb.BoletoMB;
import organizacao.dao.OrganizacaoDao;

import usuario.Usuario;
import usuario.dao.UsuarioDao;
import banco.dao.BancoDao;
import banco.tipo.TipoBanco;
import endereco.Endereco;
import endereco.dao.EnderecoDao;

@ManagedBean(name = "bancoMB")
@ViewScoped
public class BancoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Banco> bancos;
	private Banco banco = new Banco();

	@ManagedProperty(value = "#{bancoDao}")
	private BancoDao bancoDao;

	private Organizacao organizacao = new Organizacao();
	private Necessidade necessidade = new Necessidade();

	private String nomeBanco;
	private String nossoNum;
	private String agenciaBanco;
	private String numConta;
	private String numCarteira;
	private TipoBanco tipoBanco;
	private boolean isItauC = false;


	private List<Banco> bancosSelecionados = new ArrayList<Banco>();

	private Endereco endereco = new Endereco();
	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	@ManagedProperty(value = "#{boletoMB}")
	private BoletoMB boletoMB;

	private Usuario usuario = new Usuario();
	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{necessidadeDao}")
	private NecessidadeDao necessidadeDao;

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	@PostConstruct
	public void init() {
		bancos = bancoDao.listAll();
	}

	public List<Banco> pegarBancosSlecionados() {
		return bancosSelecionados;
	}

	public void salvar() {
		System.out.println("salvando Banco da organizacao - teste: ");
		if (banco.getTipoBanco() != null)
			System.out.println("nome BC: " + banco.getTipoBanco().toString());
		System.out.println("num BC: " + nossoNum);

		banco.setNome(nomeBanco);
		banco.setNossoNumero(nossoNum);
		banco.setNumCarteira(Integer.parseInt(numCarteira));
		banco.setNumeroAgencia(agenciaBanco);
		banco.setNumeroConta(numConta);
		banco.setTipoBanco(tipoBanco);

		bancoDao.salvar(banco);

		bancosSelecionados.add(banco);
		
		System.out.println("NEW no Banco --");
		//banco = new Banco();
	}

	public void salvarBancoEditar(int id) {
		banco.setOrganizacao(organizacaoDao.findById(id));
		bancoDao.salvar(banco);
	}

	public void novo() {
		System.out.println("novo banco");
		banco = new Banco();
	}

	public void atribuirNomeBanco(FacesContext fc, UIComponent component,
			Object value) {
		nomeBanco = value.toString();
		System.out.println("NOME BANCO: " + nomeBanco);
	}

	public void atribuirnossoNum(FacesContext fc, UIComponent component,
			Object value) {
		nossoNum = value.toString();
		System.out.println("NOSSO NUM: " + nossoNum);
	}

	public void atribuirAgenciaBanco(FacesContext fc, UIComponent component,
			Object value) {
		agenciaBanco = value.toString();
		System.out.println("AGENCIA BANCO: " + agenciaBanco);
	}

	public void atribuirNumCarteira(FacesContext fc, UIComponent component,
			Object value) {
		numCarteira = value.toString();
		System.out.println("NUM CARTEIRA: " + numCarteira);
	}

	public void atribuirNumConta(FacesContext fc, UIComponent component,
			Object value) {
		numConta = value.toString();
		System.out.println("NUM CONTA: " + numCarteira);
	}

	public void atribuirTipoBanco(FacesContext fc, UIComponent component,
			Object value) {
		System.out.println("TIPO BANCO: " + value.toString());
		if (value.toString() == "BANCOBRASIL"){
			System.out.println("if Ž brasil");
			tipoBanco = TipoBanco.BANCOBRASIL;
			organizacao.setBancoBrasil(true);
		}
		else if (value.toString() == "BRADESCO"){
			System.out.println("else Ž bradesco");
			tipoBanco = TipoBanco.BRADESCO;
			organizacao.setBancoBradesco(true);
		}
		else if (value.toString() == "ITAU"){
			setItauC(true);
			tipoBanco = TipoBanco.ITAU;
			organizacao.setBancoItau(true);
		}
		banco.setTipoBanco(tipoBanco);
	}

	public Usuario findUsrLogado() {
		try {
			usuario = new Usuario();
			SecurityContext context = SecurityContextHolder.getContext();
			if (context instanceof SecurityContext) {
				Authentication authentication = context.getAuthentication();
				if (authentication instanceof Authentication) {
					try {

						usuario.setUsername(context.getAuthentication()
								.getPrincipal().toString());
						usuario.setUsername(((User) authentication
								.getPrincipal()).getUsername());
						String username = usuario.getUsername();
						usuario = usuarioDao.findByLogin(username);
					} catch (Exception e) {
						this.usuario = (Usuario) context.getAuthentication()
								.getPrincipal();
						String username = usuario.getUsername();
						usuario = usuarioDao.findByLogin(username);
					}
				}
				return usuario;
			} else {
				System.out.println("nn instancia");
			}
		} catch (Exception e) {
			return null;
		}
		return usuario;

	}

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public BancoDao getBancoDao() {
		return bancoDao;
	}

	public void setBancoDao(BancoDao bancoDao) {
		this.bancoDao = bancoDao;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public Necessidade getNecessidade() {
		return necessidade;
	}

	public void setNecessidade(Necessidade necessidade) {
		this.necessidade = necessidade;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public EnderecoDao getEnderecoDao() {
		return enderecoDao;
	}

	public void setEnderecoDao(EnderecoDao enderecoDao) {
		this.enderecoDao = enderecoDao;
	}

	public BoletoMB getBoletoMB() {
		return boletoMB;
	}

	public void setBoletoMB(BoletoMB boletoMB) {
		this.boletoMB = boletoMB;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public NecessidadeDao getNecessidadeDao() {
		return necessidadeDao;
	}

	public void setNecessidadeDao(NecessidadeDao necessidadeDao) {
		this.necessidadeDao = necessidadeDao;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
	}

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
	}

	public List<Banco> getBancosSelecionados() {
		return bancosSelecionados;
	}

	public void setBancosSelecionados(List<Banco> bancosSelecionados) {
		this.bancosSelecionados = bancosSelecionados;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getAgenciaBanco() {
		return agenciaBanco;
	}

	public void setAgenciaBanco(String agenciaBanco) {
		this.agenciaBanco = agenciaBanco;
	}

	public String getNumConta() {
		return numConta;
	}

	public void setNumConta(String numConta) {
		this.numConta = numConta;
	}

	public String getNumCarteira() {
		return numCarteira;
	}

	public void setNumCarteira(String numCarteira) {
		this.numCarteira = numCarteira;
	}

	public TipoBanco getTipoBanco() {
		return tipoBanco;
	}

	public void setTipoBanco(TipoBanco tipoBanco) {
		this.tipoBanco = tipoBanco;
	}

	public String getNossoNum() {
		return nossoNum;
	}

	public void setNossoNum(String nossoNum) {
		this.nossoNum = nossoNum;
	}

	public boolean isItauC() {
		return isItauC;
	}

	public void setItauC(boolean isItauC) {
		this.isItauC = isItauC;
	}
	
	

}
