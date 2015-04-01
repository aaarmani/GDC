package br.com.actia.event;

import br.com.actia.model.Poi;


/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class IncludePoiEvent extends AbstractEvent<Poi> {

    public IncludePoiEvent(Poi poi) {
        super(poi);
    }
    
}
