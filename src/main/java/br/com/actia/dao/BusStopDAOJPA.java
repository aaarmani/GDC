/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.dao;

import br.com.actia.model.BusStop;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BusStopDAOJPA extends AbstractDAO<BusStop, Integer> implements BusStopDAO{

    public BusStopDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<BusStop> getBusStopByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT b FROM BusStop b WHERE b.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<BusStop>) query.getResultList();
    }
    
}
