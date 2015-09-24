package banco.dao;

import java.util.List;

import banco.Banco;
import banco.tipo.TipoBanco;
import dao.Dao;
import doacao.banco.BancoDoacao;

public interface BancoDao extends Dao<Banco>
{
	public List<Banco> listarBancosOrg(int id);

	public Banco findBancoBoleto(int id, BancoDoacao bancoDoacao);
	
	public Banco findBancoBoleto2(int id, BancoDoacao bancoDoacao);
	
	public Banco findBancoBoleto3(int id, BancoDoacao bancoDoacao);
	
}
