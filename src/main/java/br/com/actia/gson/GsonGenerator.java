package br.com.actia.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 * @param <T>
 */
public class GsonGenerator<T> {
    private final GsonBuilder gBuilder = new GsonBuilder();
    
    /**
     * Transform Object in String Json
     * @param entity
     * @return 
     */
    public String toJson(T entity) {
        Gson gson = new Gson();
        return gson.toJson(entity);
    }
    
    public void addAdapter(Class clss, Object adapter) {
        gBuilder.registerTypeAdapter(clss, adapter);
    }
    
    /**
     * Transform Expose attributs from Object in String Json
     * @param entity
     * @return 
     */
    public String expToJson(T entity) {
        Gson gson = gBuilder.excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(entity);
    }
    
    /**
     * Transform string Json in object
     * @param strJson
     * @param entity - Object
     * @return 
     */
    public T toEntity(String strJson, T entity) {
        Gson gson = new Gson();
        return gson.fromJson(strJson, (Class<T>) entity.getClass());
    }
    
    /**
     * Transform Objects in Json File
     * @param entity Object to transform in Json file
     * @param filePath
     * @throws IOException
     */
    public void toFile(T entity, String filePath) throws IOException {
        String fileJson = this.toJson(entity);

        FileWriter fileW = new FileWriter(new File(filePath));
        fileW.write(fileJson);
        fileW.close();
    }
    
    /**
     * Transform Objects in Json File
     * @param entity Object to transform in Json file
     * @param filePath
     * @throws IOException
     */
    public void expToFile(T entity, String filePath) throws IOException {
        String fileJson = this.expToJson(entity);

        FileWriter fileW = new FileWriter(new File(filePath));
        fileW.write(fileJson);
        fileW.close();
    }
    
    /**
     * Reads file and makes it a object
     * @param <T>
     * @param entity object to convert
     * @param filePath
     * @return Object
     * @throws IOException
     * @throws java.io.FileNotFoundException
     */
    public <T> T fileToObject(T entity, String filePath) throws IOException, FileNotFoundException {

        File file = new File(filePath);
        if(!file.exists()) {
            throw new FileNotFoundException("File not found in " + filePath);
        }
        
        Gson gson = new Gson();
        BufferedReader buf = new BufferedReader(new FileReader(file));
        return gson.fromJson(buf, (Class<T>)entity.getClass());
    }
}
