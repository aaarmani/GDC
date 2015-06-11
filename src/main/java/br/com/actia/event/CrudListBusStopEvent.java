package br.com.actia.event;

import br.com.actia.model.ListBusStop;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class CrudListBusStopEvent extends AbstractEvent<ListBusStop> {

    public CrudListBusStopEvent(ListBusStop l) {
        super(l);
    }
    
}
