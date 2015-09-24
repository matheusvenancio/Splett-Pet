package publicacao.tabela;

import java.util.ArrayList;
import java.util.List;

import publicacao.Publicacao;
import animal.Animal;

public class TabelaPublicacao {

	private List<Publicacao> publicacaos;
	private List<Animal> tipoanimais;
	
	public TabelaPublicacao() {
		tipoanimais = new ArrayList<Animal>();
		
	}

	public List<Publicacao> getPublicacaos() {
		return publicacaos;
	}

	public void setPublicacaos(List<Publicacao> publicacaos) {
		this.publicacaos = publicacaos;
	}

	public List<Animal> getTipoanimais() {
		return tipoanimais;
	}

	public void setTipoanimais(List<Animal> tipoanimais) {
		this.tipoanimais = tipoanimais;
	}
	
	
}
