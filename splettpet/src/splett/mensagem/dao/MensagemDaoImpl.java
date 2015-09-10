package splett.mensagem.dao;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import splett.dao.GenericDao;
import splett.mensagem.Mensagem;

@ManagedBean(name = "mensagemDao")
@ApplicationScoped
public class MensagemDaoImpl extends GenericDao<Mensagem> implements MensagemDao{

	private static final long serialVersionUID = 1L;

	public MensagemDaoImpl() {
		super(Mensagem.class);
	}
}
