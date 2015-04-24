/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.dao;

import br.com.actia.model.VideoType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class VideoTypeDAOJPA extends AbstractDAO<VideoType, Integer> implements VideoTypeDAO {

    public VideoTypeDAOJPA(EntityManager em) {
        super(em);
    }

    @Override
    public List<VideoType> getVideoTypeByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        Query query = getPersistenceContext().createQuery("SELECT v FROM VideoType v WHERE v.name like :name");
        query.setParameter("name", name.concat("%"));
        return (List<VideoType>) query.getResultList();
    }
    
}
