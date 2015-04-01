/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.dao;

import br.com.actia.model.PoiType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class PoiTypeDAOJPA extends AbstractDAO<PoiType, Integer> implements PoiTypeDAO {
    /**
     * @param em Recebe a referência para o <code>EntityManager</code>.
     */
    public PoiTypeDAOJPA(EntityManager em) {
        super(em);
    }
    
    /**
     * Reliza a pesquisa PoiTypes com filtro no nome (via operador
     * <code>like</code>).
     *
     * @see
     */
    @Override
    public List<PoiType> getPoiTypeByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT p FROM poi_type p WHERE p.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<PoiType>) query.getResultList();
    }
}
