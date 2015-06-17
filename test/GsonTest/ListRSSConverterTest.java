/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GsonTest;

import br.com.actia.gson.ListRSSConverter;
import br.com.actia.model.ListRSS;
import br.com.actia.model.RSS;
import java.util.ArrayList;
import java.util.List;

public class ListRSSConverterTest {
    public static void main(String[] args) {
        ListRSS listRSS = new ListRSS();
        listRSS.setId(154);
        listRSS.setName("listRSS 5");
        listRSS.setDescription("Lista de RSSs 5");
        
        List<RSS> RSSs = new ArrayList<RSS>();
        
        RSS rss1 = new RSS();
        rss1.setId(246);
        rss1.setName("RSS 11");
        rss1.setPath("http://g1.globo.com/dynamo/tecnologia/rss2.xml");

        RSSs.add(rss1);
        
        listRSS.setListRSS(RSSs);
        
        ListRSSConverter listRSSConverter = new ListRSSConverter(listRSS);
        String conversion = listRSSConverter.convert();
        
        System.out.println("Conversion: \n");
        System.out.println(conversion);
    }
}
