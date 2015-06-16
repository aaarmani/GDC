package br.com.actia.gson;

import br.com.actia.model.Route;
import br.com.actia.model.ListBanner;
import br.com.actia.model.ListRSS;
import br.com.actia.model.ListVideo;
import br.com.actia.model.ListBusStop;
import br.com.actia.model.VideoType;

public class RouteConverter {
    private final Route route;

    public RouteConverter(Route route) {
        this.route = route;
    }
    /**
     * Convert Route in JSON String
     * @return String JSON
     */
    public String convert() {
        GsonGenerator<Route> gsonGen = new GsonGenerator<>();
        
        gsonGen.addAdapter(ListBanner.class, new ListBannerAdapter());
        gsonGen.addAdapter(ListRSS.class, new ListRSSAdapter());
        gsonGen.addAdapter(ListVideo.class, new ListVideoAdapter());
        gsonGen.addAdapter(ListBusStop.class, new ListBusStopAdapter());
        
        return gsonGen.expToJson(route);
    }
    
}
