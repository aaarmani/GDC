package br.com.actia.dao;

import br.com.actia.model.ListBanner;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ListBannerDAOJPA extends AbstractDAO<ListBanner, Integer> implements ListBannerDAO {

    public ListBannerDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<ListBanner> getListBannerByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT l FROM ListBanner l WHERE l.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<ListBanner>) query.getResultList();
    }
    
}
