package br.com.actia.gson;

import br.com.actia.model.Video;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class VideoAdapter extends TypeAdapter<Video> {

    @Override
    public void write(JsonWriter writer, Video video) throws IOException {
        writer.beginObject();
        writer.name("name").value(video.getVideoName());
        writer.name("type").value(video.getType().getType());
        writer.endObject();
    }

    @Override
    public Video read(JsonReader reader) throws IOException {
        return null;
    }
    
}
