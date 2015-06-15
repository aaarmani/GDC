package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.Route;

public class CrudRouteEvent extends AbstractEvent<Route> {
    
    public CrudRouteEvent(Route r) {
        super(r);
    }
    
}
