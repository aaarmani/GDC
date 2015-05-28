package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.Video;

public class CrudVideoEvent extends AbstractEvent<Video> {
    
    public CrudVideoEvent(Video v) {
        super(v);
    }
    
}
