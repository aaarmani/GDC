package br.com.actia.gson;

import br.com.actia.model.Banner;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BannerAdapter extends TypeAdapter<Banner> {

    @Override
    public void write(JsonWriter writer, Banner banner) throws IOException {
        writer.beginObject();
        writer.name("image").value(banner.getImage());
        writer.endObject();
    }

    @Override
    public Banner read(JsonReader reader) throws IOException {
        return null;
    }
    
}
