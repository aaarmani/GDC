package br.com.actia.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table (name = "indication")
public class Indication implements AbstractEntity, Serializable {
    /**
     * Chave primária da entidade <code>BusStopIndication</code>. O valor gerado pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Size(max=16)
    @Column(unique=true, nullable=false)
    private String name;
    @NotNull
    @Expose
    private String image;
    @Transient
    private String imagePath;
    @Expose
    private String audio;
    @Transient
    private String audioPath;

    public Indication() {}
    
    public Indication(Integer id, String name, String imageName, String imagePath, String audioName, String audioPath) {
        this.id = id;
        this.name = name;
        this.image = imageName;
        this.imagePath = imagePath;
        this.audio = audioName;
        this.audioPath = audioPath;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String Audio) {
        this.audio = Audio;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImageOrigPath(String imageOrigPath) {
        this.imagePath = imageOrigPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioOrigPath(String audioOrigPath) {
        this.audioPath = audioOrigPath;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
