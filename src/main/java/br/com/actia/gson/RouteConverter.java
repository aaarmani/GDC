package br.com.actia.gson;

import br.com.actia.model.Route;
import br.com.actia.model.ListBanner;
import br.com.actia.model.ListRSS;
import br.com.actia.model.ListVideo;
import br.com.actia.model.ListBusStop;
import java.util.ArrayList;

public class RouteConverter extends AbstractConverter{
    public RouteConverter() {
        super();
        
        this.MAIN_DIRECTORY = "route";
        this.buildAdapters();
    }
    
    public RouteConverter(Route route, String destinationPath) {
        super(route, destinationPath);
        
        this.MAIN_DIRECTORY = "route";
        this.buildAdapters();
        this.path = this.CONTENT_FILES_DIRECTORY + this.SEPARATOR_CHAR + this.MAIN_DIRECTORY;
        this.filePath = this.path + this.SEPARATOR_CHAR + ((Route)this.Entity).getName() + ".json";
    }
    
    public void buildAdapters(){
        gsonGen.addAdapter(ListBanner.class, new ListBannerAdapter());
        gsonGen.addAdapter(ListRSS.class, new ListRSSAdapter());
        gsonGen.addAdapter(ListVideo.class, new ListVideoAdapter());
        gsonGen.addAdapter(ListBusStop.class, new ListBusStopAdapter());
    }
    
    public void buildFolderStructure(){
        super.destroyFolderStructure();
    }
    
    public void setRoute(Route route){
        super.setEntity(route);
    }
    
    public void setPath(String path){
        super.setContentFilesDirectory(path);
        this.path = this.CONTENT_FILES_DIRECTORY + this.SEPARATOR_CHAR + this.MAIN_DIRECTORY;
    }
    
    public void setFilePath(){
        this.filePath = this.path + this.SEPARATOR_CHAR + ((Route)this.Entity).getName() + ".json";
    }
    
    /**
     * Different method to generate all elements from Route
     * Generate file JSON
     * @return String path and file created
     */
    public String generateAllFromRoute() {
        ArrayList<String> pathsCreated = new ArrayList<String>();
        
        pathsCreated.add(this.generate());
        
        Route route = (Route)this.Entity;
        
        ListBanner listBanner = route.getBanners();
        ListBannerConverter listBannerConverter = new ListBannerConverter(listBanner, this.CONTENT_FILES_DIRECTORY);
        pathsCreated.add(listBannerConverter.generate());
        
        ListBusStop listBusStop = route.getBusStops();
        ListBusStopConverter listBusStopConverter = new ListBusStopConverter(listBusStop, this.CONTENT_FILES_DIRECTORY);
        pathsCreated.add(listBusStopConverter.generate());
        
        ListRSS listRSS = route.getRSSs();
        ListRSSConverter listRSSConverter = new ListRSSConverter(listRSS, this.CONTENT_FILES_DIRECTORY);
        pathsCreated.add(listRSSConverter.generate());
        
        ListVideo listVideo = route.getVideos();
        ListVideoConverter listVideoConverter = new ListVideoConverter(listVideo, this.CONTENT_FILES_DIRECTORY);
        pathsCreated.add(listVideoConverter.generate());
        
        return this.convertResultFromArrayListToString(pathsCreated);
    }
    
    public String convertResultFromArrayListToString(ArrayList<String> pathsCreated){
        String pathsCreatedToString = "";
        
        for(String line : pathsCreated){
            pathsCreatedToString += line + "\n";
        }
        
        return pathsCreatedToString;
    }
}
