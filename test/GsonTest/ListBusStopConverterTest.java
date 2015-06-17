/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsonTest;

import br.com.actia.gson.ListBusStopConverter;
import br.com.actia.model.Banner;
import br.com.actia.model.ListBusStop;
import br.com.actia.model.BusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.ListVideo;
import br.com.actia.model.Poi;
import br.com.actia.model.PoiType;
import br.com.actia.model.Video;
import br.com.actia.model.VideoType;
import java.util.ArrayList;
import java.util.List;

public class ListBusStopConverterTest {
    public static void main(String[] args) {
        ListBusStop listBusStop = new ListBusStop();
        listBusStop.setId(391);
        listBusStop.setName("listBusStop 18");
        
        List<BusStop> busStops = new ArrayList<BusStop>();
        
        /* BUS STOP */
            BusStop busStop = new BusStop();

            busStop.setId(123);
            busStop.setName("busStop 1");
            busStop.setDescription("Bus Stop de Teste 1");
            busStop.setLatitude(-30.12);
            busStop.setLongitude(-50.45);
            busStop.setRadius(1.6F);

            /* BANNER */
            Banner banner = new Banner();
            banner.setId(54);
            banner.setName("banner 2");
            banner.setImage("Banner de Teste 2.jpg");
            banner.setImageOrigPath("teste/teste12/Banner de Teste 2.jpg");
            banner.setAudio("Rooling Stones.mp3");
            banner.setAudioOrigPath("teste/teste45/Rooling Stones.mp3");

            busStop.setBanner(banner);

            /* LIST POI */
            ListPoi listPoi = new ListPoi();
            listPoi.setId(78);
            listPoi.setName("listPoi 3");
            listPoi.setDescription("Lista de POIs 3");

            List<Poi> pois = new ArrayList<Poi>();

            Poi poi1 = new Poi();
            poi1.setId(451);
            poi1.setName("Poi 4");
            poi1.setLatitude(-31);
            poi1.setLongitude(-51);

            PoiType poiType1 = new PoiType();
            poiType1.setId(15);
            poiType1.setName("poiType 5");
            poiType1.setType(2);

            poi1.setType(poiType1);

            pois.add(poi1);

            Poi poi2 = new Poi();
            poi2.setId(542);
            poi2.setName("Poi 6");
            poi2.setLatitude(-32);
            poi2.setLongitude(-52);

            PoiType poiType2 = new PoiType();
            poiType2.setId(23);
            poiType2.setName("poiType 7");
            poiType2.setType(1);

            poi2.setType(poiType2);

            pois.add(poi2);

            Poi poi3 = new Poi();
            poi3.setId(613);
            poi3.setName("Poi 8");
            poi3.setLatitude(-33);
            poi3.setLongitude(-53);

            PoiType poiType3 = new PoiType();
            poiType3.setId(86);
            poiType3.setName("poiType 9");
            poiType3.setType(3);

            poi3.setType(poiType3);

            pois.add(poi3);

            listPoi.setListPoi(pois);

            busStop.setPois(listPoi);

            /* LIST VIDEO */
            ListVideo listVideo2 = new ListVideo();
            listVideo2.setId(892);
            listVideo2.setName("listVideo 10");
            listVideo2.setDescription("Lista de vídeos 10");

            List<Video> videos2 = new ArrayList<Video>();

            Video video3 = new Video();
            video3.setId(931);
            video3.setName("Vídeo 11");
            video3.setVideoName("vídeoo.mp4");
            video3.setVideoPath("teste1/testeee/vídeoo.mp4");

            VideoType videoType3 = new VideoType();
            videoType3.setId(1015);
            videoType3.setName("vídeoType 12");
            videoType3.setType(1);

            video3.setType(videoType3);

            videos2.add(video3);

            listVideo2.setListVideo(videos2);

            busStop.setVideos(listVideo2);
        
        busStops.add(busStop);
        
        listBusStop.setListBusStop(busStops);
        
        ListBusStopConverter listBusStopConverter = new ListBusStopConverter(listBusStop);
        String conversion = listBusStopConverter.convert();
        
        System.out.println("Conversion: \n");
        System.out.println(conversion);
    }
}
