package br.com.actia.gson;

import br.com.actia.model.ListVideo;
import br.com.actia.model.Video;

public class ListVideoConverter extends AbstractConverter{
    public ListVideoConverter(ListVideo listVideo, String destinationPath) {
        super(listVideo, destinationPath);
        
        this.MAIN_DIRECTORY = "video";
        this.buildAdapters();
        this.path = this.CONTENT_FILES_DIRECTORY + this.SEPARATOR_CHAR + this.MAIN_DIRECTORY;
        this.filePath = this.path + this.SEPARATOR_CHAR + ((ListVideo)this.Entity).getName() + ".json";
    }
    
    public void buildAdapters(){
        gsonGen.addAdapter(Video.class, new VideoAdapter());
    }
}