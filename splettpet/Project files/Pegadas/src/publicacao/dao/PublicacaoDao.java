package publicacao.dao;

import java.util.List;

import publicacao.Publicacao;
import publicacao.tipoPublicacao.TipoPublicacao;
import animal.categoria.Categoria;
import animal.divisaoIdade.DivisaoIdade;
import animal.porte.Porte;
import animal.raca.Raca;
import animal.sexo.Sexo;
import dao.Dao;
import endereco.cidade.Cidade;
import endereco.estado.Estado;

public interface PublicacaoDao extends Dao<Publicacao> {
	public List<Publicacao> pesquisar(String tf, TipoPublicacao tipoPubli,
			Categoria categoria, Raca raca, Sexo sexo, Porte porte,
			String corPrincipal, String corSecundaria, DivisaoIdade diviIdade,
			Estado estado);

	public List<Publicacao> pesquisarT(String tf, TipoPublicacao tipoPubli,
			Categoria categoria, Estado estado);

	public List<Publicacao> pesquisarPubliByTitulo(String titulo);

	public List<Publicacao> listarPublicacoes();

	public List<Publicacao> match(TipoPublicacao tipoPubli,
			Categoria categoria, Raca raca, Sexo sexo, String cor,
			DivisaoIdade diviIdade, Estado estado, Cidade cidade);
	
	public List<Publicacao> publiByUsr(int id);

}
