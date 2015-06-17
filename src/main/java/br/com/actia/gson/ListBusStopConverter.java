package br.com.actia.gson;

import br.com.actia.model.ListBusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.ListVideo;

public class ListBusStopConverter {
    private final ListBusStop listBusStop;

    public ListBusStopConverter(ListBusStop listBusStop) {
        this.listBusStop = listBusStop;
    }
    /**
     * Convert ListBusStop in JSON String
     * @return String JSON
     */
    public String convert() {
        GsonGenerator<ListBusStop> gsonGen = new GsonGenerator<>();
        
        gsonGen.addAdapter(ListPoi.class, new ListPoiAdapter());
        gsonGen.addAdapter(ListVideo.class, new ListVideoAdapter());
        
        return gsonGen.expToJson(listBusStop);
    }
    
}
