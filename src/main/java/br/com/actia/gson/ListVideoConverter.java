package br.com.actia.gson;

import br.com.actia.model.ListVideo;
import br.com.actia.model.Video;
import br.com.actia.model.VideoType;

public class ListVideoConverter {
    private final ListVideo listVideo;

    public ListVideoConverter(ListVideo listVideo) {
        this.listVideo = listVideo;
    }
    /**
     * Convert ListVideo in JSON String
     * @return String JSON
     */
    public String convert() {
        GsonGenerator<ListVideo> gsonGen = new GsonGenerator<>();
        
        gsonGen.addAdapter(Video.class, new VideoAdapter());
        
        return gsonGen.expToJson(listVideo);
    }
    
}
