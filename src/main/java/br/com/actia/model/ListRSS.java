package br.com.actia.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table (name = "list_rss")
public class ListRSS implements AbstractEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Size(max=16)
    @Expose
    private String name;
    @NotNull
    @Size(max=48)
    @Expose
    private String description;
    @NotNull
    @ManyToMany @JoinColumn(name="rss_id", referencedColumnName="id")
    @Expose
    private List<RSS> listRSS;
    
    public ListRSS() {}

    public ListRSS(Integer id, String name, String description, List<RSS> listRSS) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.listRSS = listRSS;
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

    public List<RSS> getListRSS() {
        return listRSS;
    }

    public void setListRSS(List<RSS> listRSS) {
        this.listRSS = listRSS;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
