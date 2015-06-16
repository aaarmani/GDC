package br.com.actia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
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

@Entity
@Table (name = "route")
public class Route implements AbstractEntity, Serializable {
    /**
     * Chave primária da entidade <code>Route</code>. O valor gerado pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Size(max=16)
    @Column(unique=true, nullable=false)
    @Expose
    @SerializedName("name")
    private String name;
    @Size(max=48)
    private String description;
    @ManyToOne @JoinColumn(name="lst_banner_id", referencedColumnName="id")
    @Expose
    @SerializedName("bannerPath")
    private ListBanner banners;
    @ManyToOne @JoinColumn(name="lst_rss_id", referencedColumnName="id")
    @Expose
    @SerializedName("rssPath")
    private ListRSS RSSs;
    @ManyToOne @JoinColumn(name="lst_video_id", referencedColumnName="id")
    @Expose
    @SerializedName("videoPath")
    private ListVideo videos;
    @ManyToOne @JoinColumn(name="lst_bus_stop_id", referencedColumnName="id")
    @Expose
    @SerializedName("gpsPointPath")
    private ListBusStop busStops;

    public Route(){
    }
    
    public Route(Integer id, String name, String description, ListBanner listBanner, ListRSS listRSS, ListVideo listVideo, ListBusStop listBusStop) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.banners = listBanner;
        this.RSSs = listRSS;
        this.videos = listVideo;
        this.busStops = listBusStop;
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

    public ListBanner getBanners() {
        return banners;
    }

    public void setBanners(ListBanner banners) {
        this.banners = banners;
    }

    public ListRSS getRSSs() {
        return RSSs;
    }

    public void setRSSs(ListRSS RSSs) {
        this.RSSs = RSSs;
    }

    public ListVideo getVideos() {
        return videos;
    }

    public void setVideos(ListVideo videos) {
        this.videos = videos;
    }
    
    public ListBusStop getBusStops() {
        return busStops;
    }

    public void setBusStops(ListBusStop busStops) {
        this.busStops = busStops;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
