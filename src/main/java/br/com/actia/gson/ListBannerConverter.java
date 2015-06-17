package br.com.actia.gson;

import br.com.actia.model.ListBanner;
import br.com.actia.model.Banner;

public class ListBannerConverter {
    private final ListBanner listBanner;

    public ListBannerConverter(ListBanner listBanner) {
        this.listBanner = listBanner;
    }
    /**
     * Convert ListBanner in JSON String
     * @return String JSON
     */
    public String convert() {
        GsonGenerator<ListBanner> gsonGen = new GsonGenerator<>();
        
        gsonGen.addAdapter(Banner.class, new BannerAdapter());
        
        return gsonGen.expToJson(listBanner);
    }
    
}
