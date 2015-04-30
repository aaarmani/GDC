/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.actia.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
@Entity
@Table (name = "list_poi")
public class ListPoi implements AbstractEntity, Serializable {
    /**
     * Chave primária da entidade <code>list_video</code>. O valor gerado pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Size(max=16)
    private String name;
    @NotNull
    @Size(max=48)
    private String description;
    @NotNull
    @ManyToMany //(fetch = FetchType.EAGER) //, mappedBy = "list_poi_poi")*/
    @JoinColumn(name="poi_id", referencedColumnName="id")
    @Expose
    private List<Poi> listPoi;

    public ListPoi(){}
    
    public ListPoi(Integer id, String name, String description, List<Poi> listPoi) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.listPoi = listPoi;
    }
    
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* @ManyToMany(fetch = FetchType.EAGER, mappedBy = "poi") */
    public List<Poi> getListPoi() {
        return listPoi;
    }

    public void setListPoi(List<Poi> listPoi) {
        this.listPoi = listPoi;
    }

    @Override
    public String toString() {
        return name;
    }
}
