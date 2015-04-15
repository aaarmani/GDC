package br.com.actia.javascript.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.ListVideo;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class CrudListVideoEvent extends AbstractEvent<ListVideo>{

    public CrudListVideoEvent(ListVideo l) {
        super(l);
    }
    
}
