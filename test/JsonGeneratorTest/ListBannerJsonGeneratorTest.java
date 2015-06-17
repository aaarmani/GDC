/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonGeneratorTest;

import br.com.actia.gson.ListBannerConverter;
import br.com.actia.model.ListBanner;
import br.com.actia.model.Banner;
import java.util.ArrayList;
import java.util.List;

public class ListBannerJsonGeneratorTest {
    public static void main(String[] args) {
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
        
        ListBannerConverter listBannerConverter = new ListBannerConverter(listBanner);
        String pathCreated = listBannerConverter.generate();
        
        System.out.println("Created file: \n");
        System.out.println(pathCreated);
    }
}
