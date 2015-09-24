package doacao.mb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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

import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import doacao.Doacao;
import doacao.dao.DoacaoDao;
import endereco.Endereco;
import endereco.dao.EnderecoDao;

@ManagedBean(name = "doacaoMB")
@ViewScoped
public class DoacaoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Doacao> doacoes;
	private Doacao doacao = new Doacao();

	@ManagedProperty(value = "#{doacaoDao}")
	private DoacaoDao doacaoDao;
	private Organizacao organizacao = new Organizacao();
	private Necessidade necessidade = new Necessidade();

	private Endereco endereco = new Endereco();
	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	@ManagedProperty(value = "#{boletoMB}")
	private BoletoMB boletoMB;

	private Usuario usuario = new Usuario();
	private boolean mostrarFormDoacao = true;
	
	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{necessidadeDao}")
	private NecessidadeDao necessidadeDao;

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	@PostConstruct
	public void init() {
		doacoes = doacaoDao.listAll();
	}

	public String paginaDoacoes() {
		doacoes = doacaoDao.listAll();
		return Paginas.PAGINA_DOACOES;
	}

	public String paginaCadastroDoacao(int id) {
		organizacao = organizacaoDao.findById(id);
		return Paginas.PAGINA_CADASTRO_DOACAO;
	}

	public void novo() {
		doacao = new Doacao();
	}

	public String remover() {
		doacaoDao.remover(doacao);
		doacoes = doacaoDao.listAll();
		return Paginas.PAGINA_DOACOES;
	}

	public String cancelar() {
		doacao = null;
		return Paginas.PAGINA_DOACOES;
	}

	public String salvar(int id) {

		try {
			necessidade = necessidadeDao.findById(id);
			usuario = findUsrLogado();
			organizacao = organizacaoDao.findOrgByNecessidade(necessidade);

			doacao.setOrganizacao(organizacao);
			doacao.setUsuario(usuario);
			doacaoDao.salvar(doacao);
			necessidade.getDoacoes().add(doacao);
			necessidadeDao.update(necessidade);
			System.out.println("banco da doacao: " + doacao.getBancoDoacao());

			boletoMB.criarBoleto(doacao.getQuantiaEmDinheiro(), usuario,
					organizacao, doacao);

			return Paginas.PAGINA_ORGANIZACOES;

		} catch (Exception e) {
			System.out.println("DoacaoMB - exception - salvar");
			e.printStackTrace();
			return Paginas.PAGINA_DOACOES;
		}
	}

	public void verificaCpfExiste() {
		usuario = findUsrLogado();
		System.out.println("doa - usuario: " + usuario.getNome());
		if (usuario.getCpf() == null) {
			FacesMessage msg = new FacesMessage(
					"Atenção, "
							+ " para doar é necessário o usuário possuir um CPF válido."
							+ " Por favor informe seu CPF antes de realizar uma doação.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			mostrarFormDoacao = false;
		}
	}

	public String doarParaOrg(int id) {

		try {
			organizacao = organizacaoDao.findById(id);
			usuario = findUsrLogado();

			doacao.setOrganizacao(organizacao);
			doacao.setUsuario(usuario);
			
			//doacao.setBancoDoacao(organizacao.getBancoOrganizacao());
			doacaoDao.salvar(doacao);

			System.out.println("banco da doacao: " + doacao.getBancoDoacao());

			boletoMB.criarBoleto(doacao.getQuantiaEmDinheiro(), usuario,
					organizacao, doacao);

			return Paginas.PAGINA_ORGANIZACOES;

		} catch (Exception e) {
			System.out.println("DoacaoMB - exception - salvar");
			e.printStackTrace();
			return Paginas.PAGINA_DOACOES;
		}
	}

	public Organizacao findOrgByDoacao() {
		return null;
	}

	public Necessidade findNecessidadeByDoacao() {
		return null;
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

	public List<Doacao> getDoacoes() {
		return doacoes;
	}

	public void setDoacoes(List<Doacao> doacoes) {
		this.doacoes = doacoes;
	}

	public Doacao getDoacao() {
		return doacao;
	}

	public void setDoacao(Doacao doacao) {
		this.doacao = doacao;
	}

	public DoacaoDao getDoacaoDao() {
		return doacaoDao;
	}

	public void setDoacaoDao(DoacaoDao doacaoDao) {
		this.doacaoDao = doacaoDao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	public OrganizacaoDao getOrganizacaoDao() {
		return organizacaoDao;
	}

	public void setOrganizacaoDao(OrganizacaoDao organizacaoDao) {
		this.organizacaoDao = organizacaoDao;
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

	public Necessidade getNecessidade() {
		return necessidade;
	}

	public void setNecessidade(Necessidade necessidade) {
		this.necessidade = necessidade;
	}

	public NecessidadeDao getNecessidadeDao() {
		return necessidadeDao;
	}

	public void setNecessidadeDao(NecessidadeDao necessidadeDao) {
		this.necessidadeDao = necessidadeDao;
	}

	public boolean isMostrarFormDoacao() {
		return mostrarFormDoacao;
	}

	public void setMostrarFormDoacao(boolean mostrarFormDoacao) {
		this.mostrarFormDoacao = mostrarFormDoacao;
	}
	
	

}
