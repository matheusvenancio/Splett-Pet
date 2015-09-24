package necessidade;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import organizacao.Organizacao;

import publicacao.Publicacao;
import doacao.Doacao;

@Entity
@Table(name = "tbNecessidade")
public class Necessidade extends Publicacao {

	@Column(length = 200)
	private String descricao;

	@OneToMany
	private List<Doacao> doacoes;
	
	@ManyToOne
	private Organizacao organizacao;
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public List<Doacao> getDoacoes() {
		return doacoes;
	}

	public void setDoacoes(List<Doacao> doacoes) {
		this.doacoes = doacoes;
	}

	public Organizacao getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}

	

}
