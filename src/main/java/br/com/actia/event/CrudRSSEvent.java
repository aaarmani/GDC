package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.RSS;

public class CrudRSSEvent extends AbstractEvent<RSS> {
    
    public CrudRSSEvent(RSS r) {
        super(r);
    }
    
}
