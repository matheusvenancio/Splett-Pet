package splett.usuario.mb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import splett.criptografia.Criptografia;
import splett.usuario.TipoUsuario;
import splett.usuario.Usuario;
import splett.usuario.dao.UsuarioDao;

@ManagedBean(name = "usuarioMB")
@ViewScoped
public class UsuarioMB {

	@ManagedProperty(value = "#{usuarioDao}")
	private UsuarioDao usuarioDao;

	@ManagedProperty(value = "#{usuarioLazyDataModel}")
	private UsuarioLazyDataModel usuarioLazyDataModel;

	@ManagedProperty(value = "#{criptografia}")
	private Criptografia criptografia;

	private List<Usuario> usuariosFiltered;

	private Usuario usuario;

	public UsuarioMB() {

		usuariosFiltered = new ArrayList<Usuario>();
	}

	public void criar() {
		usuario = new Usuario();
	}

	public boolean validarLogin() {
		try {
			usuarioDao.pesquisarPorEmail(usuario.getEmail());
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Esse e-mail j� est� conectado a uma conta. Por favor, escolha outro");
			FacesContext.getCurrentInstance().addMessage("Aten��o", message);
			FacesContext.getCurrentInstance().validationFailed();
			return false;
		} catch (NoResultException nre) {
			return true;
		}
	}

	public void salvar() {
		if (usuario.getId() != null) {
			usuarioDao.update(usuario);
		} else if (validarLogin()) {
			String md5 = criptografia.criptografar(usuario.getSenha());
			usuario.setSenha(md5);
			if (usuario.getTipo() == null)
				usuario.setTipo(TipoUsuario.ROLE_USER);
			usuarioDao.salvar(usuario);
		}
	}

	public void enviarSenha() {
		try {
			usuario = usuarioDao.pesquisarPorEmail(usuario.getEmail());

			try {

				Email email = new SimpleEmail();
				definirDadosEmail(email);
				
				String senha = gerarSenha();
				enviarEmail(email, senha);

				salvarNovaSenha(senha);

			} catch (EmailException e) {
				e.printStackTrace();
			}
		} catch (NoResultException nre) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro!",
					"Esse e-mail n�o est� conectado a uma conta.");
			FacesContext.getCurrentInstance().addMessage("Aten��o", message);
		}
	}

	private void salvarNovaSenha(String senha) {
		usuario.setSenha(criptografia.criptografar(senha));
		usuarioDao.update(usuario);
	}

	private void enviarEmail(Email email, String senha) throws EmailException {
		email.setMsg("Ol� " + usuario.getNome()
				+ ", \n \n Sua nova senha de acesso �:  " + senha
				+ "\n \n Atenciosamente \n Equipe Splett");
		
		email.send();
	}

	private void definirDadosEmail(Email email) throws EmailException {
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator(
				"splettpetbeta", "perdiojogo"));
		email.setSSLOnConnect(true);
		email.setFrom("splettpetbeta@gmail.com");
		email.setSubject("SplettPet - Esqueci minha senha");
		email.addTo(usuario.getEmail());
	}

	private String gerarSenha() {
		char[] chart = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		char[] senha = new char[8];

		int chartLenght = chart.length;
		Random rdm = new Random();

		for (int x = 0; x < 8; x++)
			senha[x] = chart[rdm.nextInt(chartLenght)];

		return new String(senha);
	}

	public void remover() {
		usuarioDao.remover(usuario);
	}

	public UsuarioLazyDataModel getUsuarioLazyDataModel() {
		return usuarioLazyDataModel;
	}

	public void setUsuarioLazyDataModel(
			UsuarioLazyDataModel usuarioLazyDataModel) {
		this.usuarioLazyDataModel = usuarioLazyDataModel;
	}

	public void cancelar() {
		usuario = null;
	}

	public Criptografia getCriptografia() {
		return criptografia;
	}

	public void setCriptografia(Criptografia criptografia) {
		this.criptografia = criptografia;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuariosFiltered() {
		return usuariosFiltered;
	}

	public void setUsuariosFiltered(List<Usuario> usuariosFiltered) {
		this.usuariosFiltered = usuariosFiltered;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}
}
