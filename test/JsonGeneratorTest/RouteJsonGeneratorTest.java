/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonGeneratorTest;

import br.com.actia.gson.RouteConverter;
import br.com.actia.model.Route;
import br.com.actia.model.Banner;
import br.com.actia.model.RSS;
import br.com.actia.model.Video;
import br.com.actia.model.BusStop;
import br.com.actia.model.ListBanner;
import br.com.actia.model.ListRSS;
import br.com.actia.model.ListVideo;
import br.com.actia.model.ListBusStop;
import br.com.actia.model.ListPoi;
import br.com.actia.model.Poi;
import br.com.actia.model.PoiType;
import br.com.actia.model.VideoType;
import java.util.ArrayList;
import java.util.List;

public class RouteJsonGeneratorTest {
    public static void main(String[] args) {
        Route route = new Route();
        
        route.setId(123);
        route.setName("route 1");
        route.setDescription("Rota de Teste 1");
        
        /* LIST BANNER */
        ListBanner listBanner = new ListBanner();
        listBanner.setId(78);
        listBanner.setName("listBanner 3");
        listBanner.setDescription("Lista de Banners 3");
        
        List<Banner> banners = new ArrayList<Banner>();
        
        Banner banner1 = new Banner();
        banner1.setId(54);
        banner1.setName("banner 2");
        banner1.setImage("Banner de Teste 2.jpg");
        banner1.setImageOrigPath("teste/teste12/Banner de Teste 2.jpg");
        banner1.setAudio("Rooling Stones.mp3");
        banner1.setAudioOrigPath("teste/teste45/Rooling Stones.mp3");
        
        banners.add(banner1);
        
        Banner banner2 = new Banner();
        banner2.setId(452);
        banner2.setName("banner 3");
        banner2.setImage("Banner de Teste 3.jpg");
        banner2.setImageOrigPath("teste/teste12/Banner de Teste 3.jpg");
        banner2.setAudio("Michael Jackson.mp3");
        banner2.setAudioOrigPath("teste/teste45/Machael Jackson.mp3");
        
        banners.add(banner2);
        
        listBanner.setListBanner(banners);
        
        route.setBanners(listBanner);
        
        /* LIST RSS */
        ListRSS listRSS = new ListRSS();
        listRSS.setId(154);
        listRSS.setName("listRSS 5");
        listRSS.setDescription("Lista de RSSs 5");
        
        List<RSS> RSSs = new ArrayList<RSS>();
        
        RSS rss1 = new RSS();
        rss1.setId(246);
        rss1.setName("RSS 11");
        rss1.setPath("http://g1.globo.com/dynamo/tecnologia/rss2.xml");

//System.out.println("RSS1:");
//System.out.println(rss1.getPath());
        
        RSSs.add(rss1);
        
        listRSS.setListRSS(RSSs);
        
        route.setRSSs(listRSS);
        
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
        
        route.setVideos(listVideo);
        
        /* LIST BUS STOP */
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

            video3.setType(videoType1);

            videos2.add(video3);

            listVideo2.setListVideo(videos2);

            busStop.setVideos(listVideo2);
        
        busStops.add(busStop);
        
        listBusStop.setListBusStop(busStops);
        
        route.setBusStops(listBusStop);
        
        RouteConverter routeConverter = new RouteConverter(route);
        String pathCreated = routeConverter.generate();
        
        System.out.println("Created file: \n");
        System.out.println(pathCreated);
    }
}
