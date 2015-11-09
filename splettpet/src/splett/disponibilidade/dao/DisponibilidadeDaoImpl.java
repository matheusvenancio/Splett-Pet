package splett.disponibilidade.dao;

import java.util.Date;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import splett.dao.GenericDao;
import splett.disponibilidade.Disponibilidade;

@ManagedBean(name = "disponibilidadeDao")
@ApplicationScoped
public class DisponibilidadeDaoImpl extends GenericDao<Disponibilidade>
	implements DisponibilidadeDao {

    private static final long serialVersionUID = 1L;

    public DisponibilidadeDaoImpl() {
	super(Disponibilidade.class);
    }

    public Disponibilidade findByData(Date data) {
	EntityManager em = emf.createEntityManager();
	Query q = em.createQuery("select d from Disponibilidade d where d.data = :data");
	q.setParameter("data", data);

	Disponibilidade retorno;
	try {
	    retorno = (Disponibilidade) q.getSingleResult();
	} catch (NoResultException e) {
	    return null;
	}
	return retorno;
    }
}
