package br.com.actia.dao;

import br.com.actia.model.ListRSS;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ListRSSDAOJPA extends AbstractDAO<ListRSS, Integer> implements ListRSSDAO {

    public ListRSSDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<ListRSS> getListRSSByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT l FROM ListRSS l WHERE l.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<ListRSS>) query.getResultList();
    }
    
}
