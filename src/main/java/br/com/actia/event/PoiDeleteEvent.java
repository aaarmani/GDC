package br.com.actia.event;

import br.com.actia.model.Poi;


/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class PoiDeleteEvent extends AbstractEvent<Poi> {

    public PoiDeleteEvent(Poi poi) {
        super(poi);
    }
    
}
