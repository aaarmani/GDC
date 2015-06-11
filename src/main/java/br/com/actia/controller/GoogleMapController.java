package br.com.actia.controller;

import br.com.actia.action.AbstractAction;
import br.com.actia.dao.BusStopDAO;
import br.com.actia.dao.BusStopDAOJPA;
import br.com.actia.dao.PoiDAO;
import br.com.actia.dao.PoiDAOJPA;
import br.com.actia.event.AbstractEventListener;
import br.com.actia.event.BusStopDeleteEvent;
import br.com.actia.event.BusStopNewEvent;
import br.com.actia.event.PoiDeleteEvent;
import br.com.actia.event.PoiNewEvent;
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
import br.com.actia.model.BusStop;
import br.com.actia.model.Poi;
import br.com.actia.ui.GoogleMapView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    private final Pane parentPane;
    private final PoiController poiController;
    private final BusStopController busStopController;
    private final ListBusStopController listBusStopController;
    
    private Map<String, Marker> mapPoiMarkers = null;
    private final ResourceBundle rb;
    private Marker newMarker = null;
    //private Marker editMarker = null;
    // public MainScreenController mainScreenController;
    
    GoogleMapController(AbstractController parent, Pane pane, ResourceBundle rb) {
        super(parent);
        loadPersistenceContext(((PersistenceController) getParentController()).getPersistenceContext());
        this.rb = rb;
        
        // this.mainScreenController = (MainScreenController)parent;
        
        String htmlFile = "/html/maps.html";

        this.parentPane = pane;
        this.view = new GoogleMapView(this.rb);
        this.webView = view.getWebview();
        this.webEngine = new JavaFxWebEngine(webView.getEngine());
        JavascriptRuntime.setDefaultWebEngine(webEngine);

        this.poiController = new PoiController(this, view, this.rb);
        this.busStopController = new BusStopController(this, view, this.rb);
        this.listBusStopController = new ListBusStopController(this, view, this.rb);
        
        this.mapPoiMarkers = new HashMap<>();
        
        webView.widthProperty().addListener(e -> mapResized());
        webView.heightProperty().addListener(e -> mapResized());
        
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            setInitialized(true);
                            mapInitialized();
                        }
                    }
                });
        
        /*registerAction(view.getBtnZoomIn(), new AbstractAction() {
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
        });*/
       
        registerAction(this.view.getBtnNewBusStop(), new AbstractAction() {
            @Override
            protected void action() {
                showBusStopController(null);
            }
        });
        
        registerAction(this.view.getBtnNewPOI(), new AbstractAction() {
            @Override
            protected void action() {
                showPoiController(null);
            }
        });
        
        registerAction(this.view.getBtnNewBusStopList(), new AbstractAction() {
            @Override
            protected void action() {
                showBusStopListController(null);
            }
        });
        
        registerAction(this.view.getBtnClose(), new AbstractAction() {
            @Override
            protected void action() {
                closeMapScreen();
            }
        });
        
        registerEventListener(PoiNewEvent.class, new AbstractEventListener<PoiNewEvent>() {
            @Override
            public void handleEvent(PoiNewEvent event) {
                Poi poi = event.getTarget();
                if (poi != null) {
                    addMapMarkers(poi.getName(), new LatLong(poi.getLatitude(), poi.getLongitude()), Marker.TYPE_POI);
                }
            }
        });
        
        registerEventListener(PoiDeleteEvent.class, new AbstractEventListener<PoiDeleteEvent>() {
            @Override
            public void handleEvent(PoiDeleteEvent event) {
                Poi poi = event.getTarget();
                if(poi != null) {
                    LatLong latLong = new LatLong(poi.getLatitude(), poi.getLongitude());
                    Marker deleteMarker = mapPoiMarkers.get(latLong.toString());
                    mapPoiMarkers.remove(deleteMarker);
                    removeMarker(deleteMarker);
                }
            }
        });
        
        registerEventListener(BusStopNewEvent.class, new AbstractEventListener<BusStopNewEvent>() {
            @Override
            public void handleEvent(BusStopNewEvent event) {
                BusStop busStop = event.getTarget();
                if(busStop != null) {
                    addMapMarkers(busStop.getName(), new LatLong(busStop.getLatitude(), busStop.getLongitude()), Marker.TYPE_BUS_STOP);
                }
            }
        });
        
        registerEventListener(BusStopDeleteEvent.class, new AbstractEventListener<BusStopDeleteEvent>() {
            @Override
            public void handleEvent(BusStopDeleteEvent event) {
                BusStop busStop = event.getTarget();
                if(busStop != null) {
                    LatLong latLong = new LatLong(busStop.getLatitude(), busStop.getLongitude());
                    Marker deleteMarker = mapPoiMarkers.get(latLong.toString());
                    mapPoiMarkers.remove(deleteMarker);
                    removeMarker(deleteMarker);
                }
            }
        });
        
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
        loadPOIMarkers(null); //transformar em Thread
        loadBusStopMarkers(null); //transformar em Thread
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
    
    public boolean verifyMap(){
        if(!initialized){
            JOptionPane.showMessageDialog(new JFrame(),
                "Mapa não inicializado.",
                "Carregamento do mapa",
                JOptionPane.WARNING_MESSAGE);
            
            return false;
        } else {
            return true;
        }
    }

    void showView() {
        this.parentPane.getChildren().add(view);
    }

    public void closeView() {
        parentPane.getChildren().remove(view);
    }

    private void addBusStopList(String title) {
        listBusStopController.addBusStop(title);
    }
    
    public class JSListener {
        public void log(String text){
            System.out.println(text);
        }
    }
    
    @Override
    protected void cleanUp() {
        closeView();
        super.cleanUp();
    }
    
    //##########################################
    private void removeMarker(Marker marker) {
        if(marker != null) {
            googleMap.removeMarker(marker);
        }
    }
    
    private void removeNewMarker() {
        if(newMarker != null) {
            googleMap.removeMarker(newMarker);
            newMarker = null;
        }
    }

    private void setNewMarker(LatLong latLong) {
        removeNewMarker();
        
        String title = rb.getString("NewBusStop");
        MarkerOptions markerOptions = new MarkerOptions();
        LatLong markerLatLong = latLong;
        markerOptions.position(markerLatLong)
            .title(title)
            .animation(Animation.DROP)
            .draggable(true)
            .visible(true);
        
        newMarker = new Marker(markerOptions, Marker.TYPE_NEW_MARKER);
        googleMap.addMarker(newMarker);
        
        //set controllers position
        poiController.setPosition(newMarker.getPosition());
        busStopController.setPosition(newMarker.getPosition());
        
        this.googleMap.addUIEventHandler(newMarker, UIEventType.dragend, (JSObject obj) -> {
            LatLong latLongMarker = new LatLong((JSObject) obj.getMember("latLng"));

            poiController.setPosition(latLongMarker);
            busStopController.setPosition(latLongMarker);
        });
    }
    
    private void addMapMarkers(String name, LatLong latLong, Integer type) {
        removeNewMarker();
        
        String strIcon = "";
        switch(type) {
            case Marker.TYPE_BUS_STOP:
                strIcon = "https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/32/Map-Marker-Marker-Outside-Chartreuse.png";
                //strIcon = "https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/24/Map-Marker-Marker-Outside-Azure.png";
                //strIcon = "https://cdn4.iconfinder.com/data/icons/ios7-active-tab/512/map_marker-24.png";
                //icon: "http://maps.google.com/mapfiles/marker" + letter + ".png"
                break;
            case Marker.TYPE_POI:
                strIcon = "https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/32/Map-Marker-Flag--Right-Chartreuse.png";
                //strIcon = "https://cdn1.iconfinder.com/data/icons/Map-Markers-Icons-Demo-PNG/24/Map-Marker-Flag--Right-Azure.png";
                break;
        }
        
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
            .position(latLong)
            .title(name)
            .animation(Animation.BOUNCE)
            //.draggable(true)
            .icon(strIcon)
            .visible(true);
        
        Marker marker = new Marker(markerOptions, type);
        googleMap.addMarker(marker);
        
        mapPoiMarkers.put(latLong.toString(), marker);
        
        
        //ADD Marker Dragstart - Seta qual o marker está sendo 
        /*this.googleMap.addUIEventHandler(marker, UIEventType.dragstart, (JSObject obj) -> {
            removeNewMarker();
            LatLong latLongMarker = new LatLong((JSObject) obj.getMember("latLng"));
            editMarker = mapPoiMarkers.get(latLongMarker.toString());
        });*/
        
        //ADD Marker Dragend
        /*this.googleMap.addUIEventHandler(marker, UIEventType.dragend, (JSObject obj) -> {
            LatLong latLongMarker = new LatLong((JSObject) obj.getMember("latLng"));
            if(editMarker != null) {
                String title = editMarker.getTitle();
            
                poiController.setPosition(latLongMarker);
                busStopController.setPosition(latLongMarker);
                
                if(editMarker.getType() == Marker.TYPE_BUS_STOP) {
                    showBusStopController(title);
                }
                else {
                    showPoiController(title);    
                }
            }
        });*/
        
        //ADD Marker Click
        this.googleMap.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
            removeNewMarker();
            LatLong latLongMarker = new LatLong((JSObject) obj.getMember("latLng"));
            Marker markerItem = mapPoiMarkers.get(latLongMarker.toString());
            
            if(markerItem != null) {
                String title = markerItem.getTitle();
                
                if(markerItem.getType() == Marker.TYPE_BUS_STOP) {
                    if(listBusStopController.isVisible())
                        addBusStopList(title);
                    else
                        showBusStopController(title);
                }
                else {
                    showPoiController(title);
                }
            }
        });
    }
    
    private void loadPOIMarkers(LatLong latLong) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PoiDAO poiDAOJPA = new PoiDAOJPA(getPersistenceContext());
                List<Poi> lstPois;

                if(latLong == null)
                    lstPois = poiDAOJPA.getAll();
                else
                    lstPois = poiDAOJPA.getPoiByName(null); //alterar para getPoiByGps

                for(Poi poi : lstPois) {
                    addMapMarkers(poi.getName(), new LatLong(poi.getLatitude(), poi.getLongitude()), Marker.TYPE_POI);
                }
            }
        });
    }
    
    private void loadBusStopMarkers(LatLong latLong) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BusStopDAO busStopDAOJPA = new BusStopDAOJPA(getPersistenceContext());
                List<BusStop> lstBusStop;

                if(latLong == null)
                    lstBusStop = busStopDAOJPA.getAll();
                else
                    lstBusStop = busStopDAOJPA.getBusStopByName(null);

                for(BusStop busStop : lstBusStop) {
                    addMapMarkers(busStop.getName(), new LatLong(busStop.getLatitude(), busStop.getLongitude()), Marker.TYPE_BUS_STOP);
                }
            }
        });
    }
    
    private void showBusStopController(String busStopName) {
        poiController.closeView();
        listBusStopController.closeView();
        busStopController.showView(busStopName);
    }
    
    public void showPoiController(String poiName) {
        busStopController.closeView();
        listBusStopController.closeView();
        poiController.showView(poiName);
    }
    
    public void showBusStopListController(String busStopListName) {
        busStopController.closeView();
        poiController.closeView();
        listBusStopController.showView();
    }

    private void closeMapScreen() {
        closeView();
    }
}
