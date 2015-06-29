package br.com.actia.javascript.object;

import br.com.actia.javascript.JavascriptObject;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class Input extends JavascriptObject {
    public Input() {
        super(GMapObjectType.INPUT, "document.getElementById('pac-input')");
    }
}
