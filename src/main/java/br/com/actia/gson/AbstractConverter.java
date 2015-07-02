package br.com.actia.gson;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractConverter<T> {
    protected T Entity;
    protected final GsonGenerator<T> gsonGen;
    
    protected String jsonConversion = "";
    protected final char SEPARATOR_CHAR = File.separatorChar;
    protected String CONTENT_FILES_DIRECTORY = ""; //"CONTENT_FILES";
    protected String MAIN_DIRECTORY = null;
    protected String path = null;
    protected String filePath = null;
    
    private final List<String> directoriesNames = Arrays.asList("banner", "gps", "route", "rss", "video");
    
    public AbstractConverter(){ 
        this.Entity = null;
        this.CONTENT_FILES_DIRECTORY = "";
        this.gsonGen = new GsonGenerator<>();
    }
    
    public AbstractConverter(T Entity, String destinationPath) {
        this.Entity = Entity;
        this.CONTENT_FILES_DIRECTORY = destinationPath;
        this.gsonGen = new GsonGenerator<>();
    }
    
    /**
     * Convert Entity in JSON String
     * @return String JSON
     */
    public String convert() {
        this.jsonConversion = this.gsonGen.expToJson(Entity);
        return this.jsonConversion;
    }
    
    protected void destroyFolderStructure(){
        if(this.CONTENT_FILES_DIRECTORY != null) {
            if(Paths.get(this.CONTENT_FILES_DIRECTORY).toFile().exists()) {
                File directoryToDelete = new File(this.CONTENT_FILES_DIRECTORY);
                this.deleteFilesRecursively(directoryToDelete);
            }
        }
    }
    
    private void deleteFilesRecursively(File file){ 
        if (file.listFiles() != null) { 
            File[] files = file.listFiles(); 
            for(int i = 0 ; i < files.length ; i++){ 
                if(files[i].isDirectory() 
                        && file.getPath() == this.CONTENT_FILES_DIRECTORY
                        && this.directoriesNames.contains(files[i].getName())){
                    this.deleteFilesRecursively(files[i]);
                } else if (files[i].isFile() 
                        && file.getPath() != this.CONTENT_FILES_DIRECTORY){
                    // file.setWritable(true, true);
                    files[i].delete();
                }
            } 
        }
    }
    
    private void createFolderStructure(){
        if(this.CONTENT_FILES_DIRECTORY != null && this.path != null) {
            if(!Paths.get(this.CONTENT_FILES_DIRECTORY).toFile().exists()) {
                (new File(this.CONTENT_FILES_DIRECTORY)).mkdir();
            }
            if(!Paths.get(this.path).toFile().exists()) {
                (new File(this.path)).mkdir();
            }
        }
    }
    
    protected void setEntity(T Entity){
        this.Entity = Entity;
    }
    
    protected void setContentFilesDirectory(String path){
        this.CONTENT_FILES_DIRECTORY = path;
    }
    
    /**
     * Generate file JSON
     * @return String path and file created
     */
    public String generate() {
        this.createFolderStructure();
        
        try {
            this.gsonGen.expToFile(this.Entity, this.filePath);
            return this.filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // File arquivo = new File(this.filePath);
        // try( FileWriter fw = new FileWriter( arquivo ) ){
        //    BufferedWriter bw = new BufferedWriter(fw);             
        //    bw.write(this.jsonConversion);
        //    bw.flush();
        //}catch(IOException ex){
        //    ex.printStackTrace();
        //}
    }
    
    public abstract void buildAdapters();
}
