package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.Poi;

public class CrudPoiEvent extends AbstractEvent<Poi> {
    
    public CrudPoiEvent(Poi p) {
        super(p);
    }
    
}
