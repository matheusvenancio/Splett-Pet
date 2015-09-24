package organizacao.boleto.mb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import organizacao.Organizacao;
import organizacao.dao.OrganizacaoDao;

import sistema.Paginas;
import sistema.Sistema;
import usuario.Usuario;
import usuario.dao.UsuarioDao;
import banco.Banco;
import banco.dao.BancoDao;
import banco.tipo.TipoBanco;
import doacao.Doacao;
import endereco.dao.EnderecoDao;

@ManagedBean(name = "boletoMB")
@ViewScoped
public class BoletoMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private Boleto boleto = new Boleto();
	private Banco banco = new Banco();

	@ManagedProperty(value = "#{bancoDao}")
	private BancoDao bancoDao;

	Endereco enderecoSac = new Endereco();

	private Organizacao organizacao = new Organizacao();

	private Usuario usuario = new Usuario();

	@ManagedProperty(value = "#{organizacaoDao}")
	private OrganizacaoDao organizacaoDao;

	private Endereco endereco = new Endereco();

	@ManagedProperty(value = "#{enderecoDao}")
	private EnderecoDao enderecoDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	private String filePath;
	private StreamedContent file;

	// para o download do boleto

	@PostConstruct
	public void init() {

	}

	public StreamedContent baixarBoleto(Banco banco) {
		filePath = Sistema.PATH_PDF + banco.getUsuario().getNome() + "_"
				+ banco.getDataCadastro() + ".pdf";
		InputStream stream = null;
		try {
			stream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		file = new DefaultStreamedContent(stream, "application/pdf",
				"baixando_boleto.pdf");
		return file;
	}

	public void findBancoBoleto(int id, Doacao doacao){
		System.out.println("find banco boleto.." + doacao.getBancoDoacao().getLabel().toString());
		if (doacao.getBancoDoacao().toString() == "BANCOBRASIL"){
		banco = bancoDao.findBancoBoleto(id, doacao.getBancoDoacao());
		System.out.println("lo o banco é: " + banco.getTipoBanco());
		}
		if(doacao.getBancoDoacao().toString() == "BRADESCO"){
		banco = bancoDao.findBancoBoleto2(id, doacao.getBancoDoacao());
		System.out.println("lo o banco é: " + banco.getTipoBanco());
		}
		if (doacao.getBancoDoacao().toString() == "ITAU"){
			banco = bancoDao.findBancoBoleto3(id, doacao.getBancoDoacao());
			System.out.println("lo o banco é: " + banco.getTipoBanco());
		}
	}

	public String criarBoleto(float quantiaEmDinheiro, Usuario usuario,
			Organizacao organizacao, Doacao doacao) {
		findBancoBoleto(organizacao.getId(), doacao);
		Cedente cedente = new Cedente(organizacao.getNome(), organizacao
				.getModerador().getCpf());

		Sacado sacado = new Sacado(usuario.getNome(), usuario.getCpf());

		enderecoSac.setLocalidade("");
		enderecoSac.setCep(new CEP(organizacao.getEnderecos().get(0).getCep()));
		enderecoSac.setBairro(organizacao.getEnderecos().get(0).toString());
		enderecoSac.setLogradouro(organizacao.getEnderecos().get(0)
				.getLogradouro());
		enderecoSac.setNumero(Integer.toString(organizacao.getEnderecos()
				.get(0).getNumero()));
		sacado.addEndereco(enderecoSac);

		ContaBancaria contaBancaria = null;

		if (doacao.getBancoDoacao().toString() == "BANCOBRASIL") {
			System.out.println("é o banco do brasil");
			contaBancaria = new ContaBancaria(
					BancosSuportados.BANCO_DO_BRASIL.create());
		}  if(doacao.getBancoDoacao().toString() == "BRADESCO"){
			System.out.println("é o banco do bradesco");
			contaBancaria = new ContaBancaria(
					BancosSuportados.BANCO_BRADESCO.create());
		}
		if (doacao.getBancoDoacao().toString() == "ITAU"){
			System.out.println("é o banco itau");
			contaBancaria = new ContaBancaria(
					BancosSuportados.BANCO_ITAU.create());
		}

		String agenciadigverificador = banco.getNumeroAgencia().replaceAll(
				"^[0-9.]*-", "");
		String agencianumero = banco.getNumeroAgencia().replace(".", "")
				.replaceAll("-[0-9]", "");

		String contaNumVerificador = banco.getNumeroConta().replaceAll(
				"^[0-9.]*-", "");
		String contaNumero = banco.getNumeroConta().replace(".", "")
				.replaceAll("-[0-9]", "");

		contaBancaria.setNumeroDaConta(new NumeroDaConta(Integer
				.parseInt(contaNumero), contaNumVerificador));

		contaBancaria.setCarteira(new Carteira(banco.getNumCarteira()));
		contaBancaria.setAgencia(new Agencia(Integer.parseInt(agencianumero),
				agenciadigverificador));

		Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
		titulo.setValor(new BigDecimal(quantiaEmDinheiro));

		String nossoNumVerificador = banco.getNossoNumero().replaceAll(
				"^[0-9.]*-", "");
		String nossoNumero = banco.getNossoNumero().replace(".", "")
				.replaceAll("-[0-9]", "");

		titulo.setValor(BigDecimal.valueOf(0.23));
		titulo.setDataDoDocumento(new Date());

		Date dataAtual = new java.sql.Date(System.currentTimeMillis());
		Date dataVencimento = new Date(dataAtual.getTime()
				+ (1000 * 24 * 60 * 60 * 10));

		titulo.setDataDoVencimento(dataVencimento);
		titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
		titulo.setDesconto(new BigDecimal(0.00));
		titulo.setDeducao(BigDecimal.ZERO);
		titulo.setMora(BigDecimal.ZERO);
		titulo.setAcrecimo(BigDecimal.ZERO);
		titulo.setNossoNumero(nossoNumero);
		// 99345678912
		titulo.setDigitoDoNossoNumero(nossoNumVerificador);

		BigDecimal valorDoacao = new BigDecimal(Float.toString(doacao
				.getQuantiaEmDinheiro()));
		titulo.setValorCobrado(valorDoacao);

		if (doacao.getBancoDoacao().equals("BANCOBRASIL") || doacao.getBancoDoacao().toString() == "BANCOBRASIL") {
			System.out.println("BB: " + nossoNumero);
			titulo.setNumeroDoDocumento(banco.getNumeroBoletoBB() + "");

		}  if (doacao.getBancoDoacao().equals("BRADESCO") || doacao.getBancoDoacao().toString() == "BRADESCO"){
			System.out.println("Bradesco: " + nossoNumero);
			titulo.setNumeroDoDocumento(banco.getNumeroBoletoBradesco() + "");
		}
		if (doacao.getBancoDoacao().equals("ITAU") || doacao.getBancoDoacao().toString() == "ITAU"){
			System.out.println("Itau: " + nossoNumero);
			titulo.setNumeroDoDocumento(banco.getNumeroBoletoItau() + "");
		}

		incrementaNumBoleto(doacao);

		Boleto boleto = new Boleto(titulo);

		boleto.setInstrucaoAoSacado("Vencimento Contra a apresentacao.");
		boleto.setInstrucao2("Boleto dispensado de Juros");
		boleto.setInstrucao3("Agradecemos a Doação");

		BoletoViewer boletoViewer = new BoletoViewer(boleto);

		banco.setUsuario(findUsrLogado());
		banco.setNome(findUsrLogado().getNome() + "_"
				+ findUsrLogado().getPreferencias().getCodValidaEmail());

		File arquivoPdf = boletoViewer.getPdfAsFile(Sistema.PATH_PDF
				+ banco.getUsuario().getNome() + "_" + banco.getDataCadastro()
				+ ".pdf");
		System.out.println("Caminho absoluto" + arquivoPdf.getAbsolutePath());

		banco.setPath(arquivoPdf.getAbsolutePath());
		bancoDao.salvar(banco);
		System.out.println("chamando baixar boleto");

		filePath = Sistema.PATH_PDF + banco.getUsuario().getNome() + "_"
				+ banco.getDataCadastro() + ".pdf";
		InputStream stream = null;
		try {
			stream = new FileInputStream(arquivoPdf);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		file = new DefaultStreamedContent(stream, "application/pdf",
				"baixando_boleto.pdf");
		System.out.println("gera boleto fnanial");
		return "doacoes";
	}

	public void incrementaNumBoleto(Doacao doacao) {
		if (doacao.getBancoDoacao().equals("BANCOBRASIL") || doacao.getBancoDoacao().toString() == "BANCOBRASIL") {
			System.out.println("incrementa numero do boleto BB");
			int numBoleto = banco.getNumeroBoletoBB();
			numBoleto++;
			banco.setNumeroBoletoBB(numBoleto);
			bancoDao.update(banco);
		}  if (doacao.getBancoDoacao().equals("BRADESCO") || doacao.getBancoDoacao().toString() == "BRADESCO"){
			System.out.println("incrementa numero do boleto Bradesco");
			int numBoleto = banco.getNumeroBoletoBradesco();
			numBoleto++;
			banco.setNumeroBoletoBradesco(numBoleto);
			bancoDao.update(banco);
		}
		 if (doacao.getBancoDoacao().equals("ITAU") || doacao.getBancoDoacao().toString() == "ITAU"){
			System.out.println("incrementa numero do boleto Itau");
			int numBoleto = banco.getNumeroBoletoItau();
			numBoleto++;
			banco.setNumeroBoletoItau(numBoleto);
			bancoDao.update(banco);
		}

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

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public Endereco getEnderecoSac() {
		return enderecoSac;
	}

	public void setEnderecoSac(Endereco enderecoSac) {
		this.enderecoSac = enderecoSac;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public BancoDao getBancoDao() {
		return bancoDao;
	}

	public void setBancoDao(BancoDao bancoDao) {
		this.bancoDao = bancoDao;
	}

}
