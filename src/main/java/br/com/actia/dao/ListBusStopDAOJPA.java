package br.com.actia.dao;

import br.com.actia.model.ListBusStop;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListBusStopDAOJPA extends AbstractDAO<ListBusStop, Integer> implements ListBusStopDAO {

    public ListBusStopDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<ListBusStop> getListBusStopByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT l FROM ListBusStop l WHERE l.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<ListBusStop>) query.getResultList();
    }
}
