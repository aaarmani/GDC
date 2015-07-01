/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.dao;

import br.com.actia.dao.AbstractDAO;
import br.com.actia.dao.IndicationDAO;
import br.com.actia.model.Indication;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class IndicationDAOJPA extends AbstractDAO<Indication, Integer> implements IndicationDAO {

    public IndicationDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<Indication> getIndicationByName(String name) {
         if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT i FROM Indication i WHERE i.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<Indication>) query.getResultList();
    }
    
}
