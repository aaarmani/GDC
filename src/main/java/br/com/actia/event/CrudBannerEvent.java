package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.Banner;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class CrudBannerEvent extends AbstractEvent<Banner> {
    
    public CrudBannerEvent(Banner b) {
        super(b);
    }
    
}
