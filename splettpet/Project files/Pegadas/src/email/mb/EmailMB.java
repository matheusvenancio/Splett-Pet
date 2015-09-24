package email.mb;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import organizacao.Organizacao;

import usuario.Usuario;
import email.EmailForm;

@ManagedBean(name = "emailMB")
@ViewScoped
public class EmailMB {

	private EmailForm email_form;

	@PostConstruct
	public void load() {
		email_form = new EmailForm();
	}

	public void enviarConfirmConta(Usuario usuario) {
		try {
			HtmlEmail email = new HtmlEmail();
			StringBuffer corpo = new StringBuffer();
			corpo.append("Você recentemente se cadastrou em Pegadas Online. Para finalizar seu cadastro, é necessário "
					+ "clicar no seguinte link: <br>");
			corpo.append("<a href=\"http://localhost:8080/Pegadas/usuarios/confirmacao.ifpr?cod="
					+ usuario.getPreferencias().getCodValidaEmail()
					+ "\">"
					+ "Confirmar sua Conta </a> ou copiar o seguinte link e arrastar para o endereço de seu navegador: <br>"
					+ " http://localhost:8080/Pegadas/usuarios/confirmacao.ifpr?cod="
					+ usuario.getPreferencias().getCodValidaEmail() + " <br>");

			formatarEmail(email, corpo.toString());
			email.setSubject("Pegadas Online - Confirmação de Conta");
			email.addTo(usuario.getEmail());
			email.send();
			limpar();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public void enviarNovaPubli(Usuario usuario) {
		try {
			HtmlEmail email = new HtmlEmail();
			String corpo = "Olá, " + usuario.getNome() + ".";
			formatarEmail(email, corpo);

			email.setSubject("Pegadas Online - Criando sua Publicação");
			email.addTo(usuario.getEmail());
			email.send();
			limpar();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public void enviarConviteVolt(Usuario usuario, Organizacao organizacao) {
		try {
			HtmlEmail email = new HtmlEmail();
			StringBuffer corpo = new StringBuffer();
			corpo.append("Você recentemente recebeu um convite para tornar-se voluntário para a Organização "
					+ organizacao.getNome()
					+ " em Pegadas Online. Para aceitar, você pode acessar o sistema em sua conta e verificar sua Caixa de Entrada ou"
					+ "clicar no seguinte link: <br>");
			corpo.append("<a href=\"http://localhost:8080/Pegadas/organizacoes/convite.ifpr?to="
					+ usuario.getPreferencias().getCodValidaEmail()
					+ "&from="
					+ organizacao.getId()
					+ "\">"
					+ "Confirmar sua Conta </a> ou copiar o seguinte link e arrastar para o endereço de seu navegador: <br>"
					+ " http://localhost:8080/Pegadas/organizacoes/convite.ifpr?cod="
					+ usuario.getPreferencias().getCodValidaEmail()
					+ "&"
					+ organizacao.getId() + " <br>");

			formatarEmail(email, corpo.toString());
			email.setSubject("Pegadas Online - Você recebeu um convite!");
			email.addTo(usuario.getEmail());
			email.send();
			limpar();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public void formatarEmail(HtmlEmail email, String corpo) {
		try {

			email.setSmtpPort(587);
			email.setSslSmtpPort("587");
			email.setAuthenticator(new DefaultAuthenticator("pegadas.tcc",
					"isaisa2013"));
			email.setHostName("smtp.gmail.com");
			email.getMailSession().getProperties().put("mail.smtps.auth", true);
			email.getMailSession().getProperties().put("mail.debug", true);
			email.getMailSession().getProperties().put("mail.smtps.port", 587);
			email.getMailSession().getProperties()
					.put("mail.smtps.socketFactory.port", 587);
			email.getMailSession()
					.getProperties()
					.put("mail.smtps.socketFactory.class",
							"javax.net.ssl.SSLSocketFactory");
			email.getMailSession().getProperties()
					.put("mail.smtps.socketFactory.fallback", false);
			email.getMailSession().getProperties()
					.put("mail.smtp.starttls.enable", true);
			email.setFrom("pegadas.tcc@gmail.com", "Pegadas Online");

			// email.setSmtpPort(587);

			// email.setSSLOnConnect(true);
			// email.setSslSmtpPort("587");
			email.setDebug(true);
			// email.setStartTLSEnabled(true);
			// email.setSSL(true);
			// email.setFrom("pegadas.tcc@gmail.com");

			URL url = new URL(
					"http://localhost:8080/Pegadas/img/pegadasLogo.png");
			String cid = email.embed(url, "Pegadas Online");
			URL fundoUrl = new URL(
					"http://localhost:8080/Pegadas/img/fundoHeader.gif");
			String fundo = email.embed(fundoUrl, "Fundo");

			StringBuffer msg = new StringBuffer();
			msg.append("<html><body>");
			msg.append("<table width=\"100%\" border=\"0\"> <tr bgcolor=\"#e78f08\" background=\"cid:"
					+ fundo + "\"> <td>");
			msg.append("<center> <img src=\"cid:" + cid + "\"> </center>");
			msg.append("</td> </tr> <tr bgcolor=\"ffffff\"> <td>");
			msg.append("<font size=\"5px\">" + corpo + "</font>");
			msg.append("</td> </tr> <tr bgcolor=\"ffffff\"> <td>");
			msg.append("<font size=\"5px\"> <b> Atenciosamente, equipe Pegadas Online. </b> </font>");
			msg.append("</td> </tr>");
			msg.append("</table> </body></html>");

			email.setHtmlMsg(msg.toString());
			email.setTextMsg("Seu gerenciador de e-mails não suporta e-mails de formato HTML.");

		} catch (EmailException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		email_form = new EmailForm();
	}

	public EmailForm getEmail_form() {
		return email_form;
	}

	public void setEmail_form(EmailForm email_form) {
		this.email_form = email_form;
	}

}
