/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsonTest;

import br.com.actia.gson.ListVideoConverter;
import br.com.actia.model.ListVideo;
import br.com.actia.model.Video;
import br.com.actia.model.VideoType;
import java.util.ArrayList;
import java.util.List;

public class ListVideoConverterTest {
    public static void main(String[] args) {
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
        
        ListVideoConverter listVideoConverter = new ListVideoConverter(listVideo);
        String conversion = listVideoConverter.convert();
        
        System.out.println("Conversion: \n");
        System.out.println(conversion);
    }
}
