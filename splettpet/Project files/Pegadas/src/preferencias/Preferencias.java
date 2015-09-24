package preferencias;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbPreferencias")
public class Preferencias {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String codValidaEmail;
	private String codConviteOrg;
	private boolean isContaConfirmada;
	private boolean exibirTelefone;
	private boolean exibirEndereco;
	private boolean exibirEmail;

	private boolean emailsAtividades;
	private boolean emailsInstrucoes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodValidaEmail() {
		return codValidaEmail;
	}

	public void setCodValidaEmail(String codValidaEmail) {
		this.codValidaEmail = codValidaEmail;
	}

	public String getCodConviteOrg() {
		return codConviteOrg;
	}

	public void setCodConviteOrg(String codConviteOrg) {
		this.codConviteOrg = codConviteOrg;
	}

	public boolean isContaConfirmada() {
		return isContaConfirmada;
	}

	public void setContaConfirmada(boolean isContaConfirmada) {
		this.isContaConfirmada = isContaConfirmada;
	}

	public boolean isExibirTelefone() {
		return exibirTelefone;
	}

	public void setExibirTelefone(boolean exibirTelefone) {
		this.exibirTelefone = exibirTelefone;
	}

	public boolean isExibirEndereco() {
		return exibirEndereco;
	}

	public void setExibirEndereco(boolean exibirEndereco) {
		this.exibirEndereco = exibirEndereco;
	}

	public boolean isExibirEmail() {
		return exibirEmail;
	}

	public void setExibirEmail(boolean exibirEmail) {
		this.exibirEmail = exibirEmail;
	}

	public boolean isEmailsAtividades() {
		return emailsAtividades;
	}

	public void setEmailsAtividades(boolean emailsAtividades) {
		this.emailsAtividades = emailsAtividades;
	}

	public boolean isEmailsInstrucoes() {
		return emailsInstrucoes;
	}

	public void setEmailsInstrucoes(boolean emailsInstrucoes) {
		this.emailsInstrucoes = emailsInstrucoes;
	}

}
