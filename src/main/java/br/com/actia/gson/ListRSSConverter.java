package br.com.actia.gson;

import br.com.actia.model.ListRSS;
import br.com.actia.model.RSS;

public class ListRSSConverter {
    private final ListRSS listRSS;

    public ListRSSConverter(ListRSS listRSS) {
        this.listRSS = listRSS;
    }
    /**
     * Convert ListRSS in JSON String
     * @return String JSON
     */
    public String convert() {
        GsonGenerator<ListRSS> gsonGen = new GsonGenerator<>();
        
        gsonGen.addAdapter(RSS.class, new RSSAdapter());
        
        return gsonGen.expToJson(listRSS);
    }
    
}
