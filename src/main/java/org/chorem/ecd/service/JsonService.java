package org.chorem.ecd.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import spark.ResponseTransformer;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class JsonService implements ResponseTransformer {

    private Gson gson;

    @Override
    public String render(Object obj) {
        return getGson().toJson(obj);
    }

    public Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
            gson = gsonBuilder.create();
        }
        return gson;
    }

    public void saveToJson(Object obj, Path path) throws IOException {
        String json = getGson().toJson(obj);
        Files.createDirectories(path.getParent());
        Files.write(path, json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    }

    public <T> T loadFromJson(Class<T> type, Path path) throws IOException {
        if (Files.exists(path)) {
            String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            return getGson().fromJson(json, type);
        } else {
            return null;
        }
    }

    private static final class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            return FORMATTER.parse(jsonElement.getAsString(), LocalDateTime::from);
        }

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(FORMATTER.format(localDateTime));
        }
    }
}
