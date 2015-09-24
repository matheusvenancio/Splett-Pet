package organizacao.dao;

import java.util.Date;
import java.util.List;

import banco.Banco;

import necessidade.Necessidade;

import organizacao.Organizacao;

import animal.Animal;
import dao.Dao;
import evento.Evento;

public interface OrganizacaoDao extends Dao<Organizacao> {
	public List<Organizacao> findOrgsPendentes();

	public List<Organizacao> findOrgs();

	public Organizacao findOrgByMod(int id);
	public Organizacao findOrgByVol(int id);
	
	public List<Evento> listEventoByOrg(int id);
	public List<Evento> listUltimosEventoByOrg(int id);
	public List<Necessidade> listNecessidadesByOrg(int id);
	public List<Animal> listAnimaisByOrg(int id);
	
	public Organizacao findOrgByNecessidade(Necessidade necessidade);
	
	public Evento ultimoEventoTermino(int id);
	
	public Organizacao pesquisarOrgByNome(String nome);

	public List<Organizacao> pesquisarOrgByMod(String nomeMod);
	
	public List<Organizacao> pesquisarOrganizacoes(String nome, Date dataCadastro, String nomeMod, String estado);
	
	public Organizacao buscarOrgUsuario(int id);
	
	public List<Organizacao> listarOrgsGerenciar(String estado, boolean filtrarOrgsBanidas);
	
	public List<Organizacao> listarOrgsGerenciarPendentes(boolean filtrarOrgsP);

	public Banco verificarExisteBB(int id);
	
	public Banco verificarExisteBradesco(int id);
	
	public Banco verificarExisteItau(int id);
}
