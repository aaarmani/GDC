/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.dao;

import br.com.actia.model.Route;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class RouteDAOJPA extends AbstractDAO<Route, Integer> implements RouteDAO{

    public RouteDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<Route> getRouteByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT r FROM Route r WHERE r.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<Route>) query.getResultList();
    }
    
}
