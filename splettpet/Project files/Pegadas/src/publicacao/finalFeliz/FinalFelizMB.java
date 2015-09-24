package publicacao.finalFeliz;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import publicacao.Publicacao;
import publicacao.dao.PublicacaoDao;
import publicacao.finalFeliz.dao.FinalFelizDao;

import sistema.Paginas;
import usuario.Usuario;
import usuario.dao.UsuarioDao;

@ManagedBean(name = "finalFelizMB")
@ViewScoped
public class FinalFelizMB implements Serializable {
	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();
	private FinalFeliz finalFeliz = new FinalFeliz();
	private Publicacao publicacao;

	@ManagedProperty(value = "#{finalFelizDao}")
	private FinalFelizDao finalFelizDao;

	@ManagedProperty(value = "#{publicacaoDao}")
	private PublicacaoDao publicacaoDao;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	private List<FinalFeliz> finaisFelizes;

	@PostConstruct
	public void init() {
		finaisFelizes = finalFelizDao.listAll();
	}

	public String paginaFinaisFelizes() {
		finaisFelizes = finalFelizDao.listAll();
		return Paginas.PAGINA_LISTA_FF;
	}

	public String paginaCadastroFinalFeliz() {
		return Paginas.PAGINA_NOVO_FF;
	}

	public String salvar(int id) {

		if (finalFeliz.getId() > 0) {
			finalFelizDao.update(finalFeliz);
		} else {
			finalFeliz.setPublicacao(findPublyById(id));
			publicacao = findPublyById(id);
			publicacao.setFinalFeliz(true);
			publicacaoDao.update(publicacao);
			finalFeliz.setUsuario(findUsrLogado());
			finalFelizDao.salvar(finalFeliz);
		}
		return Paginas.PAGINA_LISTA_FF;
	}

	public Publicacao findPublyById(int id) {
		publicacao = publicacaoDao.findById(id);
		return publicacao;
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

	public List<FinalFeliz> listarFinaisFelizes() {
		finaisFelizes = finalFelizDao.listarFinaisFelizes();
		return finaisFelizes;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public FinalFeliz getFinalFeliz() {
		return finalFeliz;
	}

	public void setFinalFeliz(FinalFeliz finalFeliz) {
		this.finalFeliz = finalFeliz;
	}

	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	public FinalFelizDao getFinalFelizDao() {
		return finalFelizDao;
	}

	public void setFinalFelizDao(FinalFelizDao finalFelizDao) {
		this.finalFelizDao = finalFelizDao;
	}

	public PublicacaoDao getPublicacaoDao() {
		return publicacaoDao;
	}

	public void setPublicacaoDao(PublicacaoDao publicacaoDao) {
		this.publicacaoDao = publicacaoDao;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public List<FinalFeliz> getFinaisFelizes() {
		return finaisFelizes;
	}

	public void setFinaisFelizes(List<FinalFeliz> finaisFelizes) {
		this.finaisFelizes = finaisFelizes;
	}

}
