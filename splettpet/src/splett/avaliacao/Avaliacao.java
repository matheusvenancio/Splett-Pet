package splett.avaliacao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import splett.usuario.Usuario;

@Entity
@Table(name = "tbAvaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String texto;

    private int pontuacao;

    @JoinColumn(referencedColumnName = "id", name = "usuario_avaliador_id")
    @ManyToOne
    private Usuario avaliador;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getTexto() {
	return texto;
    }

    public void setTexto(String texto) {
	this.texto = texto;
    }

    public int getPontuacao() {
	return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
	this.pontuacao = pontuacao;
    }

    public Usuario getAvaliador() {
	return avaliador;
    }

    public void setAvaliador(Usuario avaliador) {
	this.avaliador = avaliador;
    }

}
