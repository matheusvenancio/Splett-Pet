package splett.video;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import splett.usuario.Usuario;

@Entity
@Table(name = "tbVideo")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private boolean isPublico;

    private String descricao;

    private String caminho;
    
    @OneToOne
    private Usuario usuario;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public boolean isPublico() {
	return isPublico;
    }

    public void setPublico(boolean isPublico) {
	this.isPublico = isPublico;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public String getCaminho() {
	return caminho;
    }

    public void setCaminho(String caminho) {
	this.caminho = caminho;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

}
