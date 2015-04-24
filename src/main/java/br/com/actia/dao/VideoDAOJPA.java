package br.com.actia.dao;

import br.com.actia.model.AbstractEntity;
import br.com.actia.model.Video;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class VideoDAOJPA extends AbstractDAO<Video, Integer> implements VideoDAO {

    public VideoDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<Video> getVideoByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT v FROM Video v WHERE v.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<Video>) query.getResultList();
    }
    
}
