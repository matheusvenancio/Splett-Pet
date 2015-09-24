package endereco.cidade;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import endereco.estado.Estado;

@Entity
@Table(name = "tbCidade")
public class Cidade {

	@Id
	private Integer id;
	private String nome;

	@ManyToOne
	private Estado estado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Cidade && (id != null) ? id
				.equals(((Cidade) other).getId()) : (other == this);
	}

	@Override
	public int hashCode() {
		return id != 0 ? this.getClass().hashCode() + id.hashCode() : super
				.hashCode();
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[" + id + "," + nome + "]";
	}
}
