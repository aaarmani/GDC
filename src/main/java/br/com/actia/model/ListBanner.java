package br.com.actia.model;

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
@Table (name = "list_banner")
public class ListBanner implements AbstractEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotNull
    @Size(max=16)
    private String name;
    @ManyToMany @JoinColumn(name="lst_banner_id", referencedColumnName="id")
    private List<Banner> lstBanner;
    @ManyToMany @JoinColumn(name="lst_rss_id", referencedColumnName="id")
    private List<RSS> lstRss;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Banner> getLstBanner() {
        return lstBanner;
    }

    public void setLstBanner(List<Banner> listBanner) {
        this.lstBanner = listBanner;
    }

    public List<RSS> getListRss() {
        return lstRss;
    }

    public void setListRss(List<RSS> listRss) {
        this.lstRss = listRss;
    }

    @Override
    public String toString() {
        return name;
    }
}
