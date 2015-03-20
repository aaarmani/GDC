package br.com.actia.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
@Entity
@Table(name = "poi")
public class Poi implements AbstractEntity, Serializable {
    /**
     * Chave primária da entidade <code>Poi</code>. O valor gerado pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    private Integer type;
    @NotNull
    private String name;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    
    public Poi() {
    }
    
    public Poi(Integer id, Integer type, String name, double latitude, double longitude) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    @Override
    public Number getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    @Override
    public String toString() {
        return "[ " + id +" - " + type + " - " + name + " - " + latitude + " - " + longitude + " ]";
    }
}
