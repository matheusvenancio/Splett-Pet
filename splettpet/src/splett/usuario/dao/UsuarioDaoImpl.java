package splett.usuario.dao;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
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

import splett.criptografia.Criptografia;
import splett.dao.GenericDao;
import splett.usuario.Usuario;

@SuppressWarnings("deprecation")
@ManagedBean(name = "usuarioDao")
@ApplicationScoped
public class UsuarioDaoImpl extends GenericDao<Usuario>implements UsuarioDao {

    private static final long serialVersionUID = 1L;

    public UsuarioDaoImpl() {
	super(Usuario.class);

    }

    @SuppressWarnings("unchecked")
    public List<Usuario> pesquisarPorNome(String nome) {
	EntityManager em = emf.createEntityManager();
	Query q = em.createQuery(
		"select u from Usuario u where lower(u.nome) like concat('%', :nome, '%')");
	q.setParameter("nome", nome);
	q.setMaxResults(50);
	return q.getResultList();
    }

    public Authentication authenticate(Authentication auth) {
	Usuario usuario = new Usuario();
	try {
	    usuario = pesquisarPorEmail(auth.getName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	List<GrantedAuthority> lista = new ArrayList<GrantedAuthority>();
	lista.add(new GrantedAuthorityImpl(usuario.getTipo().toString()));

	return new UsernamePasswordAuthenticationToken(usuario, auth.getCredentials(), lista);
    }

    public void realizaAutenticacaoAutomatica(HttpServletRequest request, Usuario usuario)
	    throws Exception {
	Criptografia criptografia = new Criptografia();
	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
		usuario.getEmail(), criptografia.criptografar(usuario.getSenha()));
	token.setDetails(new WebAuthenticationDetails(request));
	Authentication authentication = (Authentication) pesquisarPorEmail(usuario.getEmail());
	SecurityContextHolder.getContext().setAuthentication(authentication);
	request.getSession().setAttribute(
		HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
		SecurityContextHolder.getContext());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Usuario recoverAuthenticatedUser() {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (authentication.getPrincipal() instanceof UserDetails) {
	    Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
	    return usuarioAutenticado;
	}
	return null;
    }

    public Usuario pesquisarPorEmail(String email) {
	EntityManager em = emf.createEntityManager();
	Query q = em.createQuery("select u from Usuario u where lower(u.email) = :email");
	q.setParameter("email", email);
	return (Usuario) q.getSingleResult();
    }
    
    @SuppressWarnings("unchecked")
    public List<Usuario> pesquisarUsuarioPorCidade(String cidade , Integer id){
    	EntityManager em = emf.createEntityManager();
    	Query q = em.createQuery(
    		"select u from Usuario u inner join u.endereco e where e.cidade = :cidade and u.id != :id");
    	q.setParameter("cidade", cidade);
    	q.setParameter("id", id);
    	return q.getResultList();

    }
    
    @SuppressWarnings("unchecked")
    public List<Usuario> pesquisaUsuario(String uf, String cidade, String bairro, Integer id, String dataInicial, String dataFinal, String tipoAnimal){
    	if(bairro != null)
    	{
	    	String sqlQuery = "select tbusuario.id, tbusuario.dataNascimento, tbusuario.dataNascimento_isPublico, tbusuario.username, tbusuario.email_isPublico, tbusuario.fotoPerfil, tbusuario.genero, tbusuario.genero_isPublico, tbusuario.isFacebook, tbusuario.nome, tbusuario.nomeFotoPerfil, tbusuario.password, tbusuario.telefoneCelular, tbusuario.telefoneCelular_isPublico, tbusuario.telefoneFixo, tbusuario.telefoneFixo_isPublico, tbusuario.telefoneFixo_isPublico, tbusuario.authority, tbusuario.endereco_id, tbusuario.endereco_isPublico "
	    			+ "from tbusuario "
	    			+ "inner join tbusuariodisponibilidade on tbusuario.id = tbusuariodisponibilidade.usuario_id  "
	    			+ "inner join tbanimal on tbusuario.id = tbanimal.dono_id  "
	    			+ "inner join tbraca on tbraca.id = tbanimal.raca_id  "
	    			+ "inner join tbtipoanimal on tbtipoanimal.id = tbraca.tipoAnimal_id "
	    			+ "inner join tbdisponibilidade on tbdisponibilidade.id = tbusuariodisponibilidade.disponibilidade_id "
	    			+ "inner join tbendereco on tbendereco.id = tbusuario.endereco_id "
	    			+ "where tbendereco.uf = ? "
	    			+ "and tbendereco.cidade = ? "
	    			+ "and tbendereco.bairro = ?  "
	    			+ "and tbtipoanimal.nome = ?  "
	    			+ "and tbdisponibilidade.`data` BETWEEN ? and ? "
	    			+ "and tbusuario.id != ?";
	    	EntityManager em = emf.createEntityManager();
	    	Query q = em.createNativeQuery(sqlQuery, Usuario.class);
	    	q.setParameter(1, uf);
	    	q.setParameter(2, cidade);
	    	q.setParameter(3, bairro);
	    	q.setParameter(4, tipoAnimal);
	    	q.setParameter(5, dataInicial);
	    	q.setParameter(6, dataFinal);
	    	q.setParameter(7, id);
	    	return q.getResultList(); 
    	}
    	else{
	    	String sqlQuery = "select tbusuario.id, tbusuario.dataNascimento, tbusuario.dataNascimento_isPublico, tbusuario.username, tbusuario.email_isPublico, tbusuario.fotoPerfil, tbusuario.genero, tbusuario.genero_isPublico, tbusuario.isFacebook, tbusuario.nome, tbusuario.nomeFotoPerfil, tbusuario.password, tbusuario.telefoneCelular, tbusuario.telefoneCelular_isPublico, tbusuario.telefoneFixo, tbusuario.telefoneFixo_isPublico, tbusuario.telefoneFixo_isPublico, tbusuario.authority, tbusuario.endereco_id, tbusuario.endereco_isPublico "
	    			+ "from tbusuario "
	    			+ "inner join tbusuariodisponibilidade on tbusuario.id = tbusuariodisponibilidade.usuario_id  "
	    			+ "inner join tbanimal on tbusuario.id = tbanimal.dono_id  "
	    			+ "inner join tbraca on tbraca.id = tbanimal.raca_id  "
	    			+ "inner join tbtipoanimal on tbtipoanimal.id = tbraca.tipoAnimal_id "
	    			+ "inner join tbdisponibilidade on tbdisponibilidade.id = tbusuariodisponibilidade.disponibilidade_id "
	    			+ "inner join tbendereco on tbendereco.id = tbusuario.endereco_id "
	    			+ "where tbendereco.uf = ? "
	    			+ "and tbendereco.cidade = ? "
	    			+ "and tbtipoanimal.nome = ?  "
	    			+ "and tbdisponibilidade.`data` BETWEEN ? and ? "
	    			+ "and tbusuario.id != ?";
	    	EntityManager em = emf.createEntityManager();
	    	Query q = em.createNativeQuery(sqlQuery, Usuario.class);
	    	q.setParameter(1, uf);
	    	q.setParameter(2, cidade);
	    	q.setParameter(3, tipoAnimal);
	    	q.setParameter(4, dataInicial);
	    	q.setParameter(5, dataFinal);
	    	q.setParameter(6, id);
	    	return q.getResultList();    		
    	}
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> listUsuariosByEmail(String email) {
	EntityManager em = emf.createEntityManager();
	Query q = em.createQuery(
		"select u from Usuario u where lower(u.email) like concat('%', :email, '%')");
	q.setParameter("email", email);
	return q.getResultList();
    }










}
