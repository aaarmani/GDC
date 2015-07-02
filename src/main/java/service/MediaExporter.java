package service;

import br.com.actia.event.CopyFileEvent;
import br.com.actia.model.Banner;
import br.com.actia.model.BusStop;
import br.com.actia.model.Route;
import br.com.actia.model.Video;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.com.actia.util.CONST;

public class MediaExporter {
    List<Route> routes = null;
    ArrayList<String> banners = null;
    ArrayList<String> images = null;
    ArrayList<String> audios = null;
    ArrayList<String> videos = null;
    
    private final char SEPARATOR_CHAR = File.separatorChar;
    private String destinationFolder = null;
    // private String contentFolder = "CONTENT_FILES";
    // private String originFolder = System.getProperty("user.dir") + this.SEPARATOR_CHAR + this.contentFolder;
    private String contentFolder = CONST.getContentFolder();
    private String originFolder = System.getProperty("user.dir") + this.SEPARATOR_CHAR + this.contentFolder;
    
    private ArrayList<FileToCopy> filesToCopy = null;
    
    public MediaExporter() {
        this.banners = new ArrayList<String>();
        this.images = new ArrayList<String>();
        this.audios = new ArrayList<String>();
        this.videos = new ArrayList<String>();
        this.routes = new ArrayList<Route>();
        
        this.filesToCopy = new ArrayList<FileToCopy>();
    }
    
    public MediaExporter(List<Route> routes){
        this.routes = routes;
        this.banners = new ArrayList<String>();
        this.images = new ArrayList<String>();
        this.audios = new ArrayList<String>();
        this.videos = new ArrayList<String>();
        
        this.filesToCopy = new ArrayList<FileToCopy>();
    }
    
    public void setRoutes(List<Route> routes){
        this.routes = routes;
    }
    
    public void setDestinationFolder(String destinationFolder){
        this.destinationFolder = destinationFolder;
    }
    
    private void buildBannersMedias(){
        for(Route route : this.routes){
            for(Banner banner : route.getBanners().getListBanner()){
                if(banner.getImage() != null && !banners.contains(banner.getImage())){
                    banners.add(banner.getImage());
                }
            }
        }
    }
    
    private void buildImagesMedias(){
        for(Route route : this.routes){
            for(BusStop busStop : route.getBusStops().getListBusStop()){
                if(busStop.getIndication() != null && busStop.getIndication().getImage() != null && !images.contains(busStop.getIndication().getImage())){
                    images.add(busStop.getIndication().getImage());
                }
            }
        }
    }
    
    private void buildAudiosMedias(){
        for(Route route : this.routes){
            for(BusStop busStop : route.getBusStops().getListBusStop()){
                if(busStop.getIndication() != null && busStop.getIndication().getAudio() != null && !audios.contains(busStop.getIndication().getAudio())){
                    audios.add(busStop.getIndication().getAudio());
                }
            }
        }
    }
    
    private void buildVideosMedias(){
        for(Route route : this.routes){
            for(Video video : route.getVideos().getListVideo()){
                if(video.getVideoName() != null && !videos.contains(video.getVideoName())){
                    videos.add(video.getVideoName());
                }
            }

            for(BusStop busStop : route.getBusStops().getListBusStop()){
                for(Video video : busStop.getVideos().getListVideo()){
                    if(video.getVideoName() != null && !videos.contains(video.getVideoName())){
                        videos.add(video.getVideoName());
                    }
                }
            }
        }
    }
    
    private void createBannersFolderStructure(){
        if(!Paths.get(this.destinationFolder + this.SEPARATOR_CHAR + "banners").toFile().exists()) {
            (new File(this.destinationFolder + this.SEPARATOR_CHAR + "banners")).mkdir();
        }
    }
    
    private void createImagesFolderStructure(){
        if(!Paths.get(this.destinationFolder + this.SEPARATOR_CHAR + "images").toFile().exists()) {
            (new File(this.destinationFolder + this.SEPARATOR_CHAR + "images")).mkdir();
        }
    }
    
    private void createAudiosFolderStructure(){
        if(!Paths.get(this.destinationFolder + this.SEPARATOR_CHAR + "audios").toFile().exists()) {
            (new File(this.destinationFolder + this.SEPARATOR_CHAR + "audios")).mkdir();
        }
    }
    
    private void createVideosFolderStructure(){
        if(!Paths.get(this.destinationFolder + this.SEPARATOR_CHAR + "videos").toFile().exists()) {
            (new File(this.destinationFolder + this.SEPARATOR_CHAR + "videos")).mkdir();
        }
    }
    
    private void createFolderStructure(){
        if(this.destinationFolder != null) {
            if(Paths.get(this.destinationFolder).toFile().exists()) {
                this.createBannersFolderStructure();
                this.createImagesFolderStructure();
                this.createAudiosFolderStructure();
                this.createVideosFolderStructure();
            }
        }
    }
    
    private void copyBanners(){
        String bannerPath = "\0";
        FileToCopy fileToCopy = null;
        boolean copy = false;
        
        for(String bannerName : this.banners){
            bannerPath = this.originFolder + this.SEPARATOR_CHAR + "banners" + this.SEPARATOR_CHAR + bannerName; 
            
            copy = true;
            try {
                System.out.println("bannerPath: " + bannerPath);
                fileToCopy = new FileToCopy(FileToCopy.TYPE_IMAGE, bannerName, bannerPath);
            } catch (Exception e){
                System.out.println("Mídia não existe");
                copy = false;
                //e.printStackTrace();
            }
            
            if(copy) {
                fileToCopy.setDestFile(new File(this.destinationFolder + this.SEPARATOR_CHAR + "banners" + this.SEPARATOR_CHAR + bannerName));
                
                if( (fileToCopy.getDestFile().exists() && fileToCopy.getOrigFile().length() != fileToCopy.getDestFile().length()) || !fileToCopy.getDestFile().exists() ) {
//                    ResourceBundle rb = ResourceBundle.getBundle("languages.messages");
//                    DownloadFileTask downloadFileTask = new DownloadFileTask(rb, fileToCopy.getOrigFile(), fileToCopy.getDestFile());
//                    downloadFileTask.copyFile();
                    
                    //fireEvent(new CopyFileEvent(fileToCopy));
                    
                    this.filesToCopy.add(fileToCopy);
                }
            }
        }
    }
    
    private void copyImages(){
        String imagePath = "\0";
        FileToCopy fileToCopy = null;
        boolean copy = false;
        
        for(String imageName : this.images){
            imagePath = this.originFolder + this.SEPARATOR_CHAR + "images" + this.SEPARATOR_CHAR + imageName; 
            
            copy = true;
            try {
                System.out.println("imagePath: " + imagePath);
                fileToCopy = new FileToCopy(FileToCopy.TYPE_IMAGE, imageName, imagePath);
            } catch (Exception e){
                System.out.println("Mídia não existe");
                copy = false;
                //e.printStackTrace();
            }
            
            if(copy) {
                fileToCopy.setDestFile(new File(this.destinationFolder + this.SEPARATOR_CHAR + "images" + this.SEPARATOR_CHAR + imageName));
                
                if( (fileToCopy.getDestFile().exists() && fileToCopy.getOrigFile().length() != fileToCopy.getDestFile().length()) || !fileToCopy.getDestFile().exists() ) {
//                    ResourceBundle rb = ResourceBundle.getBundle("languages.messages");
//                    DownloadFileTask downloadFileTask = new DownloadFileTask(rb, fileToCopy.getOrigFile(), fileToCopy.getDestFile());
//                    downloadFileTask.copyFile();
                    
                    //fireEvent(new CopyFileEvent(fileToCopy));
                    
                    this.filesToCopy.add(fileToCopy);
                }
            }
        }
    }
    
    private void copyAudios(){
        String audioPath = "\0";
        FileToCopy fileToCopy = null;
        boolean copy = false;
        
        for(String audioName : this.audios){
            audioPath = this.originFolder + this.SEPARATOR_CHAR + "audios" + this.SEPARATOR_CHAR + audioName;
            
            copy = true;
            try {
                System.out.println("audioPath: " + audioPath);
                fileToCopy = new FileToCopy(FileToCopy.TYPE_AUDIO, audioName, audioPath);
            } catch (Exception e){
                System.out.println("Mídia não existe");
                copy = false;
                //e.printStackTrace();
            }
            
            if(copy) {
                fileToCopy.setDestFile(new File(this.destinationFolder + this.SEPARATOR_CHAR + "audios" + this.SEPARATOR_CHAR + audioName));
                
                if( (fileToCopy.getDestFile().exists() && fileToCopy.getOrigFile().length() != fileToCopy.getDestFile().length()) || !fileToCopy.getDestFile().exists() ) {
//                    ResourceBundle rb = ResourceBundle.getBundle("languages.messages");
//                    DownloadFileTask downloadFileTask = new DownloadFileTask(rb, fileToCopy.getOrigFile(), fileToCopy.getDestFile());
//                    downloadFileTask.copyFile();
                    
                    this.filesToCopy.add(fileToCopy);
                }
            }
        }
    }
    
    private void copyVideos(){
        String videoPath = "\0";
        FileToCopy fileToCopy = null;
        boolean copy = false;
        
        for(String videoName : this.videos){
            videoPath = this.originFolder + this.SEPARATOR_CHAR + "videos" + this.SEPARATOR_CHAR + videoName;
            
            copy = true;
            try {
                System.out.println("videoPath: " + videoPath);
                fileToCopy = new FileToCopy(FileToCopy.TYPE_VIDEO, videoName, videoPath);
            } catch (Exception e){
                System.out.println("Mídia não existe");
                copy = false;
                //e.printStackTrace();
            }
            
            if(copy) {
                fileToCopy.setDestFile(new File(this.destinationFolder + this.SEPARATOR_CHAR + "videos" + this.SEPARATOR_CHAR + videoName));
                
                if( (fileToCopy.getDestFile().exists() && fileToCopy.getOrigFile().length() != fileToCopy.getDestFile().length()) || !fileToCopy.getDestFile().exists() ) {
//                    ResourceBundle rb = ResourceBundle.getBundle("languages.messages");
//                    DownloadFileTask downloadFileTask = new DownloadFileTask(rb, fileToCopy.getOrigFile(), fileToCopy.getDestFile());
//                    downloadFileTask.copyFile();
                    
                    this.filesToCopy.add(fileToCopy);
                }
            }
        }
    }
    
    private void copyAllFiles(){
        this.buildBannersMedias();
        this.buildImagesMedias();
        this.buildAudiosMedias();
        this.buildVideosMedias();
        
        this.copyBanners();
        this.copyImages();
        this.copyAudios();
        this.copyVideos();
    }
    
    private void deleteLeftoverBanners(){
        String[] filesList = null;
        
        String bannersPath = this.destinationFolder + this.SEPARATOR_CHAR + "banners";
        
        filesList = new File(bannersPath).list();
        
        File fileToDelete = null;
        ArrayList<File> filesToDelete = new ArrayList<File>();
        for(String fileName : filesList){
            if(!this.banners.contains(fileName)){
                System.out.println("Deleting file: " + fileName);
                fileToDelete = new File(bannersPath + this.SEPARATOR_CHAR + fileName);
                filesToDelete.add(fileToDelete);
            }
        }
        
        Thread threadDelBanners = new Thread(new Runnable() {
            @Override
            public void run() {
                for(File fileToDelete : filesToDelete){
                    try {
                        fileToDelete.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadDelBanners.setDaemon(true);
        threadDelBanners.start();
    }
    
    private void deleteLeftoverImages(){
        String[] filesList = null;
        
        String imagesPath = this.destinationFolder + this.SEPARATOR_CHAR + "images";
        
        filesList = new File(imagesPath).list();
        
        File fileToDelete = null;
        ArrayList<File> filesToDelete = new ArrayList<File>();
        for(String fileName : filesList){
            if(!this.images.contains(fileName)){
                System.out.println("Deleting file: " + fileName);
                fileToDelete = new File(imagesPath + this.SEPARATOR_CHAR + fileName);
                filesToDelete.add(fileToDelete);
            }
        }
        
        Thread threadDelImages = new Thread(new Runnable() {
            @Override
            public void run() {
                for(File fileToDelete : filesToDelete){
                    try {
                        fileToDelete.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadDelImages.setDaemon(true);
        threadDelImages.start();
    }
    
    private void deleteLeftoverAudios(){
        String[] filesList = null;
        
        String audiosPath = this.destinationFolder + this.SEPARATOR_CHAR + "audios";
        
        filesList = new File(audiosPath).list();
        
        File fileToDelete = null;
        ArrayList<File> filesToDelete = new ArrayList<File>();
        for(String fileName : filesList){
            if(!this.audios.contains(fileName)){
                System.out.println("Deleting file: " + fileName);
                fileToDelete = new File(audiosPath + this.SEPARATOR_CHAR + fileName);
                filesToDelete.add(fileToDelete);
            }
        }
        
        Thread threadDelAudios = new Thread(new Runnable() {
            @Override
            public void run() {
                for(File fileToDelete : filesToDelete){
                    try {
                        fileToDelete.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadDelAudios.setDaemon(true);
        threadDelAudios.start();
    }
    
    private void deleteLeftoverVideos(){
        String[] filesList = null;
        
        String videosPath = this.destinationFolder + this.SEPARATOR_CHAR + "videos";
        
        filesList = new File(videosPath).list();
        
        File fileToDelete = null;
        ArrayList<File> filesToDelete = new ArrayList<File>();
        for(String fileName : filesList){
            if(!this.videos.contains(fileName)){
                System.out.println("Deleting file: " + fileName);
                fileToDelete = new File(videosPath + this.SEPARATOR_CHAR + fileName);
                filesToDelete.add(fileToDelete);
            }
        }
        
        Thread threadDelVideos = new Thread(new Runnable() {
            @Override
            public void run() {
                for(File fileToDelete : filesToDelete){
                    try {
                        fileToDelete.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadDelVideos.setDaemon(true);
        threadDelVideos.start();
    }
    
    private void deleteLeftoverFiles(){
        this.deleteLeftoverBanners();
        this.deleteLeftoverImages();
        this.deleteLeftoverAudios();
        this.deleteLeftoverVideos();
    }
    
    public ArrayList<FileToCopy> exportAndBuild(){
        this.createFolderStructure();
        this.copyAllFiles();
        this.deleteLeftoverFiles();
        
        return this.getFilesToCopy();
    }
    
    private ArrayList<FileToCopy> getFilesToCopy(){
        return this.filesToCopy;
    }
}
