package endereco.estado;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbEstado")
public class Estado {

	@Id
	private Integer id;
	private String nome;
	private String uf;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Estado && (id != null) ? id
				.equals(((Estado) other).getId()) : (other == this);
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
