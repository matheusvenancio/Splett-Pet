package usuario.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import publicacao.Publicacao;
import usuario.Usuario;
import dao.Dao;

public interface UsuarioDao extends Dao<Usuario> {
	public Usuario findByLoginSenha(String login, String senha)
			throws UnsupportedEncodingException, NoSuchAlgorithmException;

	public Usuario findByEmail(String email);

	public Usuario findByLogin(String username);

	public void realizaAutenticacaoAutomatica(
			HttpServletRequest servletRequest, Usuario usuario)
			throws Exception;

	public Usuario recoverAuthenticatedUser();

	public List<Publicacao> findPblByUsr(int idUsr);

	public Usuario findByCod(String cod);
	
	 public Authentication authenticate(Authentication auth);
	 
	 public List<Usuario> buscarUsuariosBanidos();
	 
}
