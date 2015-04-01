package br.com.actia.dao;

import br.com.actia.model.Poi;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class PoiDAOJPA  extends AbstractDAO<Poi, Integer> implements PoiDAO {
    /**
     * @param em Recebe a referência para o <code>EntityManager</code>.
     */
    public PoiDAOJPA(EntityManager em) {
        super(em);
    }
    
    /**
     * Reliza a pesquisa POIs com filtro no nome (via operador
     * <code>like</code>).
     *
     * @see
     */
    @Override
    public List<Poi> getPoiByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT p FROM poi p WHERE p.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<Poi>) query.getResultList();
    }

    /**
     * Realiza a pesquisa de POIs com filtro por localização
     */
    /*@Override
    public List<Poi> getPoiByLatLong(Double latitude, Double longitude, Double radius) {
        if(latitude == null || longitude == null || radius == null) {
            return null;
        }
        
        Query query = getPersistenceContext().createQuery("SELECT p FROM Poi p WHERE p.name like :name");
        query.setParameter("nome", name.concat("%"));
        return (List<Poi>) query.getResultList();
    }*/
}
