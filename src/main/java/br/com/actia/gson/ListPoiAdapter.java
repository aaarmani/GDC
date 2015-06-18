package br.com.actia.gson;

import br.com.actia.model.Poi;
import br.com.actia.model.ListPoi;
import br.com.actia.model.PoiType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListPoiAdapter extends TypeAdapter<ListPoi>{
    
    final GsonBuilder gBuilder = new GsonBuilder().registerTypeAdapter(PoiType.class, new PoiTypeAdapter());
    final Gson embedded = gBuilder.excludeFieldsWithoutExposeAnnotation().create();
    
    @Override
    public void write(JsonWriter writer, ListPoi listPoi) throws IOException {
        // embedded.toJson(embedded.toJsonTree(listPoi.getListPoi()), writer);
        
        writer.beginArray();
        
        for(Poi poi: listPoi.getListPoi()){  
            writer.beginObject();
            writer.name("name").value(poi.getName());
            writer.name("type").value(poi.getType().getType());
            writer.endObject();
        }
        
        writer.endArray();
    }

    @Override
    public ListPoi read(JsonReader reader) throws IOException {
        return null;
    }
}
