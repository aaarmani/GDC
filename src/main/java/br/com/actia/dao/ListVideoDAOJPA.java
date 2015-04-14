package br.com.actia.dao;

import br.com.actia.model.ListVideo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListVideoDAOJPA extends AbstractDAO<ListVideo, Integer> implements ListVideoDAO {

    public ListVideoDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<ListVideo> getListVideoByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT l FROM list_video l WHERE l.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<ListVideo>) query.getResultList();
    }
    
}
