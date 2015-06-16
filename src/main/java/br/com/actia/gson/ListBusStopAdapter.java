package br.com.actia.gson;

import br.com.actia.model.ListBusStop;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ListBusStopAdapter extends TypeAdapter<ListBusStop> {

    @Override
    public void write(JsonWriter writer, ListBusStop listBusStop) throws IOException {
        writer.value(listBusStop.getName() + ".json");
    }

    @Override
    public ListBusStop read(JsonReader reader) throws IOException {
        return null;
    }
    
}
