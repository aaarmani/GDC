/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsonTest;

import br.com.actia.gson.BusStopConverter;
import br.com.actia.model.Banner;
import br.com.actia.model.BusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.ListVideo;
import br.com.actia.model.Poi;
import br.com.actia.model.PoiType;
import br.com.actia.model.Video;
import br.com.actia.model.VideoType;
import java.util.ArrayList;
import java.util.List;

public class BusStopConverterTest {
    public static void main(String[] args) {
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
        ListVideo listVideo = new ListVideo();
        listVideo.setId(892);
        listVideo.setName("listVideo 10");
        listVideo.setDescription("Lista de vídeos 10");
        
        List<Video> videos = new ArrayList<Video>();
        
        Video video1 = new Video();
        video1.setId(931);
        video1.setName("Vídeo 11");
        video1.setVideoName("vídeoo.mp4");
        video1.setVideoPath("teste1/testeee/vídeoo.mp4");
        
        VideoType videoType1 = new VideoType();
        videoType1.setId(1015);
        videoType1.setName("vídeoType 12");
        videoType1.setType(1);
        
        video1.setType(videoType1);
        
        videos.add(video1);
        
        Video video2 = new Video();
        video2.setId(1153);
        video2.setName("Vídeo 13");
        video2.setVideoName("vídeoo2.mp4");
        video2.setVideoPath("teste1/testeee/vídeoo2.mp4");
        
        VideoType videoType2 = new VideoType();
        videoType2.setId(1264);
        videoType2.setName("vídeoType 14");
        videoType2.setType(2);
        
        video2.setType(videoType2);
        
        videos.add(video2);
        
        listVideo.setListVideo(videos);
        
        busStop.setVideos(listVideo);
        
        BusStopConverter busStopConverter = new BusStopConverter(busStop);
        String conversion = busStopConverter.convert();
        
        System.out.println("Conversion: \n");
        System.out.println(conversion);
    }
}
