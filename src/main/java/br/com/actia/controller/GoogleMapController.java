package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.dao.PoiDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.IncludePoiEvent;
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
import br.com.actia.model.Poi;
import br.com.actia.ui.GoogleMapView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
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
    
    private PoiController poiController;
    private BusStopController busStopController;
    
    private Map<String, Marker> mapPoiMarkers = null;
    private final ResourceBundle rb;
    
    // public MainScreenController mainScreenController;
    
    GoogleMapController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        
        // this.mainScreenController = (MainScreenController)parent;
        
        String htmlFile = "/html/maps.html";

        this.view = new GoogleMapView(this.rb);
        this.webView = view.getWebview();
        this.webEngine = new JavaFxWebEngine(webView.getEngine());
        JavascriptRuntime.setDefaultWebEngine(webEngine);

        this.poiController = new PoiController(this, view, this.rb);
        this.busStopController = new BusStopController(this, view, this.rb);
        
        this.mapPoiMarkers = new HashMap<String, Marker>();
        
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
        
        registerAction(view.getBtnZoomIn(), new AbstractAction() {
            @Override
            protected void action() {
                setZoom(googleMap.getZoom()+1);
            }
        });
        
        registerAction(view.getBtnZoomOut(), new AbstractAction() {
            @Override
            protected void action() {
                setZoom(googleMap.getZoom()-1);
            }
        });
       
        registerAction(this.view.getBtnNewBusStop(), new AbstractAction() {

            @Override
            protected void action() {
                showBusStopController();
            }
        });
        
        registerAction(this.view.getBtnNewPOI(), new AbstractAction() {

            @Override
            protected void action() {
                showPoiController(null);
            }
        });
        
        registerAction(this.view.getBtnClose(), new AbstractAction() {

            @Override
            protected void action() {
                closeMapScreen();
            }
        });
        
        registerEventListener(IncludePoiEvent.class, new AbstractEventListener<IncludePoiEvent>() {
            @Override
            public void handleEvent(IncludePoiEvent event) {
                Poi poi = event.getTarget();
                if (poi != null) {
                    addMapMarkers(poi);
                }
            }
        });
        
        //pane.setCenter(this.view);
        pane.getChildren().add(this.view);
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
                setNewMarker(ll);
            }
        });
        
        //List All Markers
        loadMarkers(null);
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
    
    @Override
    protected void cleanUp() {
        poiController.cleanUp();
        busStopController.cleanUp();
        
        super.cleanUp(); //To change body of generated methods, choose Tools | Templates.
    }
    
    //##########################################
    private void removeNewMarker() {
        if(newMarker != null) {
            googleMap.removeMarker(newMarker);
        }
    }

    private void setNewMarker(LatLong latLong) {
        removeNewMarker();

        String title = rb.getString("NewBusStop");
        Poi poi = new Poi(null, null, title, latLong.getLatitude(), latLong.getLongitude());
        
        MarkerOptions markerOptions = new MarkerOptions();
        LatLong markerLatLong = latLong;
        markerOptions.position(markerLatLong)
            .title(title)
            .animation(Animation.DROP)
            .draggable(true)
            .visible(true);

        newMarker = new Marker(markerOptions);//, poi);
        googleMap.addMarker(newMarker);
        
        //set controllers position
        poiController.setPosition(newMarker.getPosition());
        busStopController.setPosition(newMarker.getPosition());
    }
    
    private void addMapMarkers(Poi poi) {
        removeNewMarker();
        
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
            .position(new LatLong(poi.getLatitude(), poi.getLongitude()))
            .title(poi.getName())
            .animation(Animation.DROP)
            //.icon("https://cdn4.iconfinder.com/data/icons/ios7-active-tab/512/map_marker-512.png")
            .visible(true);
       
        Marker marker = new Marker(markerOptions);//, poi);
        googleMap.addMarker(marker);
        mapPoiMarkers.put(poi.getName(), marker);
        
        this.googleMap.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
            //System.out.println("CLICK ON MARKER!!! ====> " + );
            
            LatLong latLong = new LatLong((JSObject) obj.getMember("latLng"));
           
            String title = (String) obj.getMember("title");
            showPoiController(title);
        });
    }
    
    private void loadMarkers(LatLong latLong) {
        PoiDAOJPA poiDAOJPA = new PoiDAOJPA(getPersistenceContext());
        List<Poi> lstPois;
        
        if(latLong == null)
            lstPois = poiDAOJPA.getAll();
        else
            lstPois = poiDAOJPA.getPoiByName(null);
        
        for(Poi poi : lstPois) {
            addMapMarkers(poi);
        }
    }
    
    private void showBusStopController() {
        poiController.closeView();
        busStopController.showView();
    }
    
    private void showPoiController(String poiName) {
        busStopController.closeView();
        poiController.showView(poiName);
    }
    
    private void closeMapScreen() {
        closeView();
        //this.mainScreenController.cleanUpGoogleMapController();
    }
    
    private void closeView() {
        this.view.setVisible(false);
    }
}
