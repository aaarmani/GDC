package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.BusStop;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class BusStopNewEvent extends AbstractEvent<BusStop> {
    
    public BusStopNewEvent(BusStop b) {
        super(b);
    }
    
}
