/*
 * Copyright 2014 Lynden, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.actia.javascript.object;

import br.com.actia.javascript.JavascriptObject;
import br.com.actia.model.Poi;

/**
 * Marker which can be placed on a GoogleMap.
 * 
 * @author Rob Terpilowski
 */
public class Marker extends JavascriptObject {
    protected String title;
    protected LatLong latLong = null;
    //protected Poi poi = null;
    
    
    /**
     * Contructs a new map Marker with the specified options
     * @param markerOptions The options to use when constructing this marker.
     */
    public Marker(MarkerOptions markerOptions){//, Poi poi) {
        super(GMapObjectType.MARKER, markerOptions);
        this.latLong = markerOptions.position;
        this.title = markerOptions.title;
        //this.poi = poi;
    }
    
    /**
     * Sets the title of this Marker
     * @param title The Marker's new title
     */
    public void setTitle( String title ) {
        invokeJavascript("setTitle", title);
        this.title = title;
    }
    
    
    public String getTitle() {
        return this.title;
    }
    
    /**
     * This method is called from the GoogleMap.addMarker() method, it should not be invoked directory.
     *
     * @param map The map to add this Marker to.
     */
    protected void setMap( GoogleMap map ) {
        invokeJavascript("setMap", map);
    }
    
    
    /**
     * Sets how the marker should be animated.  To clear the animation use Animation.NULL
     * @param animation The animation to use for this marker.
     */
    public void setAnimation( Animation animation ) {
        invokeJavascript("setAnimation", animation);
    }
    
    
    public void setPosition( LatLong latLong ) {
        this.latLong = latLong;
        invokeJavascript( "setPosition", latLong );
    }
    
    public LatLong getPosition() {
        return this.latLong;
    }
    
    /*public void setPoi(Poi poi) {
        this.poi = poi;
    }
    
    public Poi getPoi() {
        return this.poi;
    }*/
    
}
