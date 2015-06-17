package br.com.actia.gson;

import br.com.actia.model.Route;
import br.com.actia.model.ListBanner;
import br.com.actia.model.ListRSS;
import br.com.actia.model.ListVideo;
import br.com.actia.model.ListBusStop;

public class RouteConverter extends AbstractConverter{
    public RouteConverter(Route route) {
        super(route);
        
        this.MAIN_DIRECTORY = "route";
        this.buildAdapters();
        this.path = this.CONTENT_FILES_DIRECTORY + this.SEPARATOR_CHAR + this.MAIN_DIRECTORY;
        this.filePath = this.path + this.SEPARATOR_CHAR + ((Route)this.Entity).getName() + ".json";
    }
    
    public void buildAdapters(){
        gsonGen.addAdapter(ListBanner.class, new ListBannerAdapter());
        gsonGen.addAdapter(ListRSS.class, new ListRSSAdapter());
        gsonGen.addAdapter(ListVideo.class, new ListVideoAdapter());
        gsonGen.addAdapter(ListBusStop.class, new ListBusStopAdapter());
    }
}
