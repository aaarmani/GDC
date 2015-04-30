package br.com.actia.gson;

import br.com.actia.model.VideoType;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author Armani <anderson.armani@actia.com.br>
 */
public class VideoTypeAdapter extends TypeAdapter<VideoType> {

    @Override
    public void write(JsonWriter writer, VideoType videoType) throws IOException {
        writer.value(videoType.getType());
    }

    @Override
    public VideoType read(JsonReader reader) throws IOException {
        return null;
    }
}
