package br.com.actia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
@Entity
@Table(name = "poi")
public class Poi implements AbstractEntity {
    public static final Integer POI_DEFAULT = 0;
    
    /**
     * Chave primária da entidade <code>Poi</code>. O valor gerado pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="poi_type_id", referencedColumnName="id")
    private PoiType type;
    @NotNull
    @Size(max=16)
    @Column(unique=true, nullable=false)  
    private String name;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    
    public Poi() {
    }
    
    public Poi(Integer id, PoiType type, String name, double latitude, double longitude) {
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
    
    public PoiType getType() {
        return type;
    }
    
    public void setType(PoiType type) {
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
        return name;
    }
}
