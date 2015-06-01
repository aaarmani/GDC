package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.ListRSS;

public class CrudListRSSEvent extends AbstractEvent<ListRSS>{

    public CrudListRSSEvent(ListRSS l) {
        super(l);
    }
    
}
