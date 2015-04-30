package br.com.actia.gson;

import br.com.actia.model.BusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.ListVideo;
import br.com.actia.model.PoiType;
import br.com.actia.model.VideoType;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BusStopConverter {
    private final BusStop busStop;

    public BusStopConverter(BusStop busStop) {
        this.busStop = busStop;
    }
    /**
     * Convert BusStop in JSON String
     * @return String JSON
     */
    public String convert() {
        GsonGenerator<BusStop> gsonGen = new GsonGenerator<>();
        gsonGen.addAdapter(PoiType.class, new PoiTypeAdapter());
        gsonGen.addAdapter(ListVideo.class, new ListVideoAdapter());
        gsonGen.addAdapter(ListPoi.class, new ListPoiAdapter());
        
        return gsonGen.expToJson(busStop);
    }
    
    /**
     * Get list of videos from this BusStop
     * @return 
     */
    public String getVideoList() {
        GsonGenerator<ListVideo> gsonGen = new GsonGenerator<>();
        gsonGen.addAdapter(VideoType.class, new VideoTypeAdapter());
        
        return gsonGen.expToJson(busStop.getVideos());
    }
}
