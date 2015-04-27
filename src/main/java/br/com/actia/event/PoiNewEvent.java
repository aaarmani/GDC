package br.com.actia.event;

import br.com.actia.model.Poi;


/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class PoiNewEvent extends AbstractEvent<Poi> {

    public PoiNewEvent(Poi poi) {
        super(poi);
    }
    
}
