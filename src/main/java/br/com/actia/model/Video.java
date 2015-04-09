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
@Table (name="video")
public class Video implements AbstractEntity {
    /**
     * Chave primária da entidade <code>Video</code>. O valor gerado pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Size(max=16)
    @Column(unique=true, nullable=false)  
    private String name;
    @ManyToOne
    @JoinColumn(name="video_type_id", referencedColumnName="id")
    private VideoType type;
    @NotNull
    private String filePath;

    public Video() {}

    public Video(Integer id, VideoType type, String name, String filePath) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.filePath = filePath;
    }
    
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VideoType getType() {
        return type;
    }

    public void setType(VideoType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return name;
    }
}
