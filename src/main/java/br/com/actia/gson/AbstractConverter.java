package br.com.actia.gson;

import java.io.File;
import java.nio.file.Paths;

public class AbstractConverter<T> {
    protected final T Entity;
    protected final GsonGenerator<T> gsonGen;
    
    protected String jsonConversion = "\0";
    protected final char SEPARATOR_CHAR = File.separatorChar;
    protected final String CONTENT_FILES_DIRECTORY = "CONTENT_FILES";
    protected String MAIN_DIRECTORY = null;
    protected String path = null;
    protected String filePath = null;
    
    public AbstractConverter(T Entity) {
        this.Entity = Entity;
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
}
