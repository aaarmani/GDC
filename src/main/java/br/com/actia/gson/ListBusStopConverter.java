package br.com.actia.gson;

import br.com.actia.model.ListBusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.ListVideo;

public class ListBusStopConverter extends AbstractConverter{
    public ListBusStopConverter(ListBusStop listBusStop, String destinationPath) {
        super(listBusStop, destinationPath);
        
        this.MAIN_DIRECTORY = "gps";
        this.buildAdapters();
        this.path = this.CONTENT_FILES_DIRECTORY + this.SEPARATOR_CHAR + this.MAIN_DIRECTORY;
        this.filePath = this.path + this.SEPARATOR_CHAR + ((ListBusStop)this.Entity).getName() + ".json";
    }
    
    public void buildAdapters(){
        this.gsonGen.addAdapter(ListPoi.class, new ListPoiAdapter());
        this.gsonGen.addAdapter(ListVideo.class, new ListVideoAdapter());
    }
}


