package arquivo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbArquivo")
public class Arquivo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private String nomeReal;
	private String path;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeReal() {
		return nomeReal;
	}

	public void setNomeReal(String nomeReal) {
		this.nomeReal = nomeReal;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Arquivo && (id != null) ? id
				.equals(((Arquivo) other).getId()) : (other == this);
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
