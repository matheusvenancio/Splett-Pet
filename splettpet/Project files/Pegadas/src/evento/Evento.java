package evento;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import organizacao.Organizacao;

import publicacao.Publicacao;
import endereco.Endereco;

@Entity
@Table(name="tbEvento")
@DiscriminatorValue("evento")

public class Evento extends Publicacao{
	
	private Date dataInicioEvento;
	private Date dataTerminoEvento;
	@Column(length=300)
	private String descricao;
	
			
	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name="idEndereco")
	private Endereco endereco;
	
	
	
	@ManyToOne
	private Organizacao organizacao;
	
	public String getDescricao() {
		return descricao;
	}
	public Date getDataInicioEvento() {
		return dataInicioEvento;
	}
	public void setDataInicioEvento(Date dataInicioEvento) {
		this.dataInicioEvento = dataInicioEvento;
	}
	public Date getDataTerminoEvento() {
		return dataTerminoEvento;
	}
	public void setDataTerminoEvento(Date dataTerminoEvento) {
		this.dataTerminoEvento = dataTerminoEvento;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	
	public Organizacao getOrganizacao() {
		return organizacao;
	}
	public void setOrganizacao(Organizacao organizacao) {
		this.organizacao = organizacao;
	}
	
	
	
	
}
