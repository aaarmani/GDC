package br.com.actia.dao;

import br.com.actia.model.AbstractEntity;
import br.com.actia.model.RSS;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class RSSDAOJPA extends AbstractDAO<RSS, Integer> implements RSSDAO {

    public RSSDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<RSS> getRSSByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT r FROM RSS r WHERE r.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<RSS>) query.getResultList();
    }
    
}
