package br.com.actia.gson;

import br.com.actia.model.RSS;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class RSSAdapter extends TypeAdapter<RSS> {

    @Override
    public void write(JsonWriter writer, RSS RSS) throws IOException {
        writer.beginObject();
        writer.name("path").value(RSS.getPath());
        writer.endObject();
    }

    @Override
    public RSS read(JsonReader reader) throws IOException {
        return null;
    }
    
}
