package br.com.actia.event;

import br.com.actia.event.AbstractEvent;
import br.com.actia.model.ListBanner;

public class CrudListBannerEvent extends AbstractEvent<ListBanner>{

    public CrudListBannerEvent(ListBanner l) {
        super(l);
    }
    
}
