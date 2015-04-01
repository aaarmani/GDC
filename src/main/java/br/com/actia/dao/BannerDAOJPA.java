/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.dao;

import br.com.actia.dao.AbstractDAO;
import br.com.actia.dao.BannerDAO;
import br.com.actia.model.Banner;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BannerDAOJPA extends AbstractDAO<Banner, Integer> implements BannerDAO {

    public BannerDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<Banner> getBannerByName(String name) {
         if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT b FROM banner b WHERE b.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<Banner>) query.getResultList();
    }
    
}
