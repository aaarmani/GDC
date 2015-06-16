package br.com.actia.gson;

import br.com.actia.model.ListRSS;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ListRSSAdapter extends TypeAdapter<ListRSS> {

    @Override
    public void write(JsonWriter writer, ListRSS listRSS) throws IOException {
        writer.value(listRSS.getName() + ".json");
    }

    @Override
    public ListRSS read(JsonReader reader) throws IOException {
        return null;
    }
    
}
