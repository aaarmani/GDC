package br.com.actia.gson;

import br.com.actia.model.PoiType;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class PoiTypeAdapter extends TypeAdapter<PoiType> {

    @Override
    public void write(JsonWriter writer, PoiType poiType) throws IOException {
        writer.value(poiType.getType());
    }

    @Override
    public PoiType read(JsonReader reader) throws IOException {
        return null;
    }
}
