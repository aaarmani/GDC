package br.com.actia.javascript.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.ListPoi;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class CrudListPoiEvent extends AbstractEvent<ListPoi> {

    public CrudListPoiEvent(ListPoi l) {
        super(l);
    }
    
}
