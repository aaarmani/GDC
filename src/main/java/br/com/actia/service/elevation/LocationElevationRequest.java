/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.actia.service.elevation;

import br.com.actia.javascript.JavascriptObject;
import br.com.actia.javascript.object.GMapObjectType;
import br.com.actia.javascript.object.LatLong;

/** Creates a request that can be passed in to the {@link ElevationService} to 
 * get the elevations for a number of locations.
 *
 * @author Geoff Capper
 */
public class LocationElevationRequest extends JavascriptObject {
    
    public LocationElevationRequest() {
        super(GMapObjectType.OBJECT);
    }
    
    public LocationElevationRequest(LatLong[] locations) {
        super(GMapObjectType.OBJECT);
        getJSObject().setMember("locations", getJSObject().eval("[]"));
        for (int i = 0; i < locations.length; i++) {
            getJSObject().eval(getVariableName()+".locations.push("+locations[i].getVariableName()+")");
        }
    }
    
}
