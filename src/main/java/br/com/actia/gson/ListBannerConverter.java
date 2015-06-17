package br.com.actia.gson;

import br.com.actia.model.ListBanner;
import br.com.actia.model.Banner;

public class ListBannerConverter extends AbstractConverter{
    public ListBannerConverter(ListBanner listBanner) {
        super(listBanner);
        
        this.MAIN_DIRECTORY = "banner";
        this.buildAdapters();
        this.path = this.CONTENT_FILES_DIRECTORY + this.SEPARATOR_CHAR + this.MAIN_DIRECTORY;
        this.filePath = this.path + this.SEPARATOR_CHAR + ((ListBanner)this.Entity).getName() + ".json";
    }
    
    public void buildAdapters(){
        this.gsonGen.addAdapter(Banner.class, new BannerAdapter());
    }
}
