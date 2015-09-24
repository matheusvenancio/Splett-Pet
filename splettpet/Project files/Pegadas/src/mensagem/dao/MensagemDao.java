package mensagem.dao;

import java.util.List;

import usuario.Usuario;

import mensagem.Mensagem;
import dao.Dao;

public interface MensagemDao extends Dao<Mensagem>{

	public List<Mensagem> getCaixaEntrada(Usuario destinatario);
	public List<Mensagem> getCaixaSaida(Usuario remetente);
	public List<Mensagem> getMsgsNovas(Usuario destinatario);
}
