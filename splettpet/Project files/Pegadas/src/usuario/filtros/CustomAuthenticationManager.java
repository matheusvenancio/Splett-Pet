package usuario.filtros;

import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import usuario.Usuario;
import usuario.dao.UsuarioDao;
import usuario.mb.SenhaMd5MB;


public class CustomAuthenticationManager implements AuthenticationManager {

	protected static Logger logger = Logger.getLogger("CustomAuthenticationManager");
	private Usuario usuario;

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;
	
	
	@Override
	public Authentication authenticate(Authentication auth) {

		try {
			usuario = usuarioDao.findByLogin(auth.getName());
		} catch (Exception e) {
			logger.error("Usuario nao existe!", e);
			throw new Error("Ocorreu um erro ao recuperar o usuario!");
		}

		if (usuario != null) {
						
			try {
				SenhaMd5MB senhaMd5Mb = new SenhaMd5MB();
				if (!senhaMd5Mb.criptografar((String) auth.getCredentials()).equals(usuario.getPassword())) {
					logger.error("Senha incorreta!");
					throw new BadCredentialsException("Senha incorreta!");
				}
			} catch (Exception e) {
				logger.error("Erro ao realizar a validacao da senha!");
				throw new BadCredentialsException("Erro ao realizar a validacao da senha!");
			}

			if (auth.getName().equals(auth.getCredentials())) {
				logger.debug("O usuario informado e a senha sao os mesmos!");
				throw new BadCredentialsException("O usuario informado e a senha sao os mesmos!");

			} else {
				return new UsernamePasswordAuthenticationToken(usuario, auth.getCredentials(), ((Authentication) usuario).getAuthorities());
			}
		} else {
			throw new BadCredentialsException("Usuario nao existe!");
		}
	}
}