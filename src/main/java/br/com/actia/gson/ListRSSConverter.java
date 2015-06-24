package br.com.actia.gson;

import br.com.actia.model.ListRSS;
import br.com.actia.model.RSS;

public class ListRSSConverter extends AbstractConverter{
    public ListRSSConverter(ListRSS listRSS, String destinationPath) {
        super(listRSS, destinationPath);
        
        this.MAIN_DIRECTORY = "rss";
        this.buildAdapters();
        this.path = this.CONTENT_FILES_DIRECTORY + this.SEPARATOR_CHAR + this.MAIN_DIRECTORY;
        this.filePath = this.path + this.SEPARATOR_CHAR + ((ListRSS)this.Entity).getName() + ".json";
    }
    
    public void buildAdapters(){
        gsonGen.addAdapter(RSS.class, new RSSAdapter());
    }
}