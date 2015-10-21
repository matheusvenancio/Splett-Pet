package splett.disponibilidade.dao;

import java.util.Date;

import splett.dao.Dao;
import splett.disponibilidade.Disponibilidade;

public interface DisponibilidadeDao extends Dao<Disponibilidade> {

    public Disponibilidade findByData(Date data);

}
