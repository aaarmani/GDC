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

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
@Entity
@Table (name = "list_busstop")
public class ListBusStop implements AbstractEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Size(max=16)
    @Expose
    private String name;
    @NotNull
    @ManyToMany @JoinColumn(name="busstop_id", referencedColumnName="id")
    @Expose
    private List<BusStop> listBusStop;
    
    public ListBusStop() {}
    
    public ListBusStop(Integer id, String name, List<BusStop> listBusStop) {
        this.id = id;
        this.name = name;
        this.listBusStop = listBusStop;
    }

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

    public List<BusStop> getListBusStop() {
        return listBusStop;
    }

    public void setListBusStop(List<BusStop> listBusStop) {
        this.listBusStop = listBusStop;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
