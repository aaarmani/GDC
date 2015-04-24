package br.com.actia.model;

import java.io.Serializable;
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
@Table (name = "bus_stop")
public class BusStop implements AbstractEntity, Serializable {
    /**
     * Chave primária da entidade <code>BusStop</code>. O valor gerado pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Size(max=16)
    @Column(unique=true, nullable=false)
    private String name;
    @Size(max=48)
    private String description;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    @NotNull
    private float radius;
    @ManyToOne @JoinColumn(name="banner_id", referencedColumnName="id")
    private Banner banner;
    @ManyToOne @JoinColumn(name="lst_poi_id", referencedColumnName="id")
    private ListPoi pois;
    @ManyToOne @JoinColumn(name="lst_video_id", referencedColumnName="id")
    private ListVideo videos;

    public BusStop(){
    }
    
    public BusStop(Integer id, String name, String description, Double latitude, Double longitude, float radius, Banner banner, ListPoi listPoi, ListVideo listVideo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.banner = banner;
        this.pois = listPoi;
        this.videos = listVideo;
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public ListPoi getPois() {
        return pois;
    }

    public void setPois(ListPoi pois) {
        this.pois = pois;
    }

    public ListVideo getVideos() {
        return videos;
    }

    public void setVideos(ListVideo videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return name;
    }
}
