package br.com.actia.gson;

import br.com.actia.model.ListVideo;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class ListVideoAdapter extends TypeAdapter<ListVideo> {

    @Override
    public void write(JsonWriter writer, ListVideo listVideo) throws IOException {
        writer.value(listVideo.getName() + ".json");
    }

    @Override
    public ListVideo read(JsonReader reader) throws IOException {
        return null;
    }
    
}
