package br.com.actia.gson;

import br.com.actia.model.ListBanner;
import br.com.actia.model.Banner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class ListBannerJsonGenerator {
    private final ListBanner listBanner;
    private ListBannerConverter listBannerConverter;
    private String jsonConversion;
    private final String CONTENT_FILES_DIRECTORY = "CONTENT_FILES";
    private final String BANNER_DIRECTORY = "banner";
    private final char SEPARATOR_CHAR = File.separatorChar;
    private String path;
    private String filePath;
    
    public ListBannerJsonGenerator(ListBanner listBanner) {
        this.jsonConversion = "\0";
        this.listBanner = listBanner;
        this.path = this.CONTENT_FILES_DIRECTORY + this.SEPARATOR_CHAR + this.BANNER_DIRECTORY;
        this.filePath = this.path + this.SEPARATOR_CHAR + this.listBanner.getName() + ".json";
        this.buildConverter();
    }
    
    private void buildConverter(){
        this.listBannerConverter = new ListBannerConverter(this.listBanner);
    }
    
    private void createFolderStructure(){
        if(!Paths.get(this.CONTENT_FILES_DIRECTORY).toFile().exists()) {
            (new File(this.CONTENT_FILES_DIRECTORY)).mkdir();
        }
        
        if(!Paths.get(this.path).toFile().exists()) {
            (new File(this.path)).mkdir();
        }
    }
    
    /**
     * Convert ListBanner in JSON String
     * and Generate file JSON
     * @return String path and file created
     */
    public String generate() {
        this.jsonConversion = this.listBannerConverter.convert();
        
        GsonGenerator gsonGenerator = new GsonGenerator();
        
        // this.createFolderStructure();
        
        // File arquivo = new File(this.filePath);
        // try( FileWriter fw = new FileWriter( arquivo ) ){
        //    BufferedWriter bw = new BufferedWriter(fw);             
        //    bw.write(this.jsonConversion);
        //    bw.flush();
        //}catch(IOException ex){
        //    ex.printStackTrace();
        //}
        
        return this.filePath;
    }
}
