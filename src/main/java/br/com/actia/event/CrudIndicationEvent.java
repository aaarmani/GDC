package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.Indication;

public class CrudIndicationEvent extends AbstractEvent<Indication> {
    
    public CrudIndicationEvent(Indication i) {
        super(i);
    }
    
}
