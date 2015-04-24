/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table (name = "list_video")
public class ListVideo implements AbstractEntity, Serializable {
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
    @ManyToMany @JoinColumn(name="video_id", referencedColumnName="id")
    private List<Video> listVideo;

    public ListVideo() {}

    public ListVideo(Integer id, String name, String description, List<Video> listVideo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.listVideo = listVideo;
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

    public List<Video> getListVideo() {
        return listVideo;
    }

    public void setListVideo(List<Video> listVideo) {
        this.listVideo = listVideo;
    }

    @Override
    public String toString() {
        return name;
    }
}
