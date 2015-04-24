package br.com.actia.dao;

import br.com.actia.model.ListPoi;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListPoiDAOJPA extends AbstractDAO<ListPoi, Integer> implements ListPoiDAO {

    public ListPoiDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<ListPoi> getListPoiByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT l FROM ListPoi l WHERE l.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<ListPoi>) query.getResultList();
    }
    
}
