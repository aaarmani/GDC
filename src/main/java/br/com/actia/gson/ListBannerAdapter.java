package br.com.actia.gson;

import br.com.actia.model.ListBanner;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class ListBannerAdapter extends TypeAdapter<ListBanner> {

    @Override
    public void write(JsonWriter writer, ListBanner listBanner) throws IOException {
        writer.value(listBanner.getName() + ".json");
    }

    @Override
    public ListBanner read(JsonReader reader) throws IOException {
        return null;
    }
    
}
