package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.javascript.JavaFxWebEngine;
import br.com.actia.javascript.JavascriptRuntime;
import br.com.actia.javascript.event.MapStateEventType;
import br.com.actia.javascript.event.UIEventType;
import br.com.actia.javascript.object.Animation;
import br.com.actia.javascript.object.GoogleMap;
import br.com.actia.javascript.object.LatLong;
import br.com.actia.javascript.object.MapOptions;
import br.com.actia.javascript.object.MapTypeIdEnum;
import br.com.actia.javascript.object.Marker;
import br.com.actia.javascript.object.MarkerOptions;
import br.com.actia.ui.GoogleMapView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.Point2D;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;


/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class GoogleMapController extends PersistenceController {
    protected GoogleMapView view;
    protected WebView webView;
    protected JavaFxWebEngine webEngine;
    protected GoogleMap googleMap;
    protected boolean initialized = false;
    private Marker newMarker = null;
    
    private IncludePoiController poiController;
    private IncludeBusStopController busStopController;
    
    GoogleMapController(AbstractController parent, BorderPane pane) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        
        String htmlFile = "/html/maps.html";

        this.view = new GoogleMapView();
        this.webView = view.getWebview();
        this.webEngine = new JavaFxWebEngine(webView.getEngine());
        JavascriptRuntime.setDefaultWebEngine(webEngine);

        this.poiController = new IncludePoiController(this, view);
        this.busStopController = new IncludeBusStopController(this, view);
        
        webView.widthProperty().addListener(e -> mapResized());
        webView.heightProperty().addListener(e -> mapResized());
        
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            setInitialized(true);
                            mapInitialized();
                        }
                    }
                });
        
        this.view.getBtnZoomIn().setOnAction((event) -> {
            setZoom(googleMap.getZoom()+1);
            System.out.println("BOTAO ZOOM IN PRESSIONADO = " + googleMap.getZoom());
        });
        
        this.view.getBtnZoomOut().setOnAction((event) -> {
            setZoom(googleMap.getZoom()-1);
            System.out.println("BOTAO ZOOM OUT PRESSIONADO = " + googleMap.getZoom());
        });
        
        this.view.getBtnNewBusStop().setOnAction((event) -> {
            poiController.closeView();
            busStopController.showView();
        });
        
        this.view.getBtnNewPOI().setOnAction((event) -> {
            busStopController.closeView();
            poiController.showView();
        });
        
        pane.setCenter(this.view);
        webEngine.load(getClass().getResource(htmlFile).toExternalForm());
    }

    public void mapInitialized() {
        //Inicializa o mapa centralizado na Actia do Brasil
        LatLong center = new LatLong(-30.005320, -51.203227);
        
        MapOptions options = new MapOptions();
        options.center(center)
                .mapMarker(true)
                .zoom(14)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .mapType(MapTypeIdEnum.ROADMAP);

        googleMap = createMap(options);
        googleMap.setHeading(123.2);
        
        
        //### Define listners
        this.googleMap.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
            if(ll != null) {
                System.out.println("LatLong: lat: " + ll.getLatitude() + " lng: " + ll.getLongitude());
                setNewMarker(ll);
            }
        });
    }
    
   private void mapResized() {
        if (initialized) {
            //googleMap.triggerResized();
            webEngine.executeScript("google.maps.event.trigger("+googleMap.getVariableName()+", 'resize')");
        }
    }
    
    public void setZoom(int zoom) {
        checkInitialized();
        googleMap.setZoom(zoom);
    }

    public void setCenter(double latitude, double longitude) {
        checkInitialized();
        LatLong latLong = new LatLong(latitude, longitude);
        googleMap.setCenter(latLong);
    }

    public GoogleMap getMap() {
        checkInitialized();
        return googleMap;
    }

    public GoogleMap createMap( MapOptions mapOptions ) {
        checkInitialized();
        googleMap = new GoogleMap(mapOptions);
        googleMap.addStateEventHandler(MapStateEventType.projection_changed, () -> {
            if (googleMap.getProjection() != null) {
                mapResized();
                //fireMapReadyListeners();
            }
        });
        
        return googleMap;
    }

    public GoogleMap createMap() {
        googleMap = new GoogleMap();
        return googleMap;
    }
    
    public Point2D fromLatLngToPoint(LatLong loc) {
        checkInitialized();
        return googleMap.fromLatLngToPoint(loc);
    }
    
    public void panBy(double x, double y) {
        checkInitialized();
        googleMap.panBy(x, y);
    }

    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
    
    protected JSObject executeJavascript(String function) {
        Object returnObject = webEngine.executeScript(function);
        return (JSObject) returnObject;
    }

    protected String getJavascriptMethod(String methodName, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName).append("(");
        for (Object arg : args) {
            sb.append(arg).append(",");
        }
        sb.replace(sb.length() - 1, sb.length(), ")");

        return sb.toString();
    }

    protected void checkInitialized() {
        if (!initialized) {
            throw new RuntimeException("Mapa não inicializado");
        }
    }

    public class JSListener {
        public void log(String text){
            System.out.println(text);
        }
    }
    
    
    //###
    private void setNewMarker(LatLong latLong) {
        if(newMarker != null) {
            googleMap.removeMarker(newMarker);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        LatLong markerLatLong = latLong;
        markerOptions.position(markerLatLong)
            .title("Novo Ponto de Parada")
            .animation(Animation.DROP)
            .visible(true);

        newMarker = new Marker(markerOptions);
        googleMap.addMarker(newMarker);
        
        //set controllers position
        poiController.setPosition(newMarker.getPosition());
        busStopController.setPosition(newMarker.getPosition());
    }

    @Override
    protected void cleanUp() {
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
}
