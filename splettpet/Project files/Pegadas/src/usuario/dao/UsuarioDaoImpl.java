package usuario.dao;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import publicacao.Publicacao;
import usuario.Usuario;
import usuario.mb.SenhaMd5MB;
import dao.GenericDao;

@ManagedBean(name = "usuarioDao")
@ApplicationScoped
public class UsuarioDaoImpl extends GenericDao<Usuario> implements UsuarioDao {
	private static final long serialVersionUID = 1L;

	public UsuarioDaoImpl() {
		super(Usuario.class);
	}

	@Override
	public Usuario findByLoginSenha(String username, String password) {

		Usuario usuario;
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select u from Usuario u where u.username = :username and u.password = :password");
		query.setParameter("username", username);
		query.setParameter("password", password);

		usuario = (Usuario) query.getSingleResult();
		return usuario;
	}

	public Usuario findByEmail(String email) {
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select u from Usuario u where u.email = :email");
			query.setParameter("email", email);

			return (Usuario) query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		}
	}

	public Usuario findByCod(String cod) {
		System.out.println("Cod convite org: " + cod);
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select u from Usuario u where u.id = (select p.id from Preferencias p where p.codValidaEmail = :cod)");
		query.setParameter("cod", cod);

		return (Usuario) query.getSingleResult();
	}

	@Override
	public Usuario findByLogin(String username) {

		try {
			EntityManager em = emf.createEntityManager();
			Query query = em
					.createQuery("select u from Usuario u where u.username = :username");
			query.setParameter("username", username);

			return (Usuario) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Publicacao> findPblByUsr(int idUsr) {
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select p from Publicacao p where p.usuario.id = :idUsr order by p.dataCadastro desc");
		query.setParameter("idUsr", idUsr);

		return query.getResultList();
	}

	// FACEBOOK

	public void realizaAutenticacaoAutomatica(HttpServletRequest request,
			Usuario usuario) throws Exception {
		SenhaMd5MB senhamd5 = new SenhaMd5MB();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				usuario.getUsername(), senhamd5.criptografar(usuario
						.getPassword()));
		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = (Authentication) findByLogin(usuario
				.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		request.getSession()
				.setAttribute(
						HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
						SecurityContextHolder.getContext());
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Usuario recoverAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication.getPrincipal() instanceof UserDetails) {
			Usuario usuarioAutenticado = (Usuario) authentication
					.getPrincipal();
			return usuarioAutenticado;
		}
		return null;
	}

	public Authentication authenticate(Authentication auth) {
		Usuario usuario = new Usuario();
		try {
			usuario = findByEmail(auth.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<GrantedAuthority> lista = new ArrayList<GrantedAuthority>();
		lista.add(new GrantedAuthorityImpl(usuario.getAuthority()));

		return new UsernamePasswordAuthenticationToken(usuario,
				auth.getCredentials(), lista);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> buscarUsuariosBanidos() {
		System.out.println("ADSD");
		EntityManager em = emf.createEntityManager();
		Query query = em
				.createQuery("select u from Usuario u where u.isBanido = true");
		
		return (List<Usuario>) query.getResultList();
	}

}
