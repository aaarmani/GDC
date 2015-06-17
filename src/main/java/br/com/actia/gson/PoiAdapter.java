package br.com.actia.gson;

import br.com.actia.model.Poi;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class PoiAdapter extends TypeAdapter<Poi> {

    @Override
    public void write(JsonWriter writer, Poi poi) throws IOException {
        writer.beginObject();
        writer.name("name").value(poi.getName());
        writer.name("type").value(poi.getType().getType());
        writer.endObject();
    }

    @Override
    public Poi read(JsonReader reader) throws IOException {
        return null;
    }
    
}
