package common.util;

import common.models.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.LinkedHashMap;

/**
 * Утилита для сериализации/десериализации коллекции в JSON.
 */
public class JsonSerializer {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    /**
     * Сохраняет коллекцию в JSON файл.
     */
    public static void saveCollection(LinkedHashMap<String, Person> collection, String filename) throws IOException {
        Type type = new TypeToken<LinkedHashMap<String, Person>>() {}.getType();
        String json = gson.toJson(collection, type);
        Path path = Path.of(filename);
        Files.writeString(path, json, StandardCharsets.UTF_8);
        System.out.println("Коллекция сохранена в " + filename);
    }

    /**
     * Загружает коллекцию из JSON файла.
     */
    public static LinkedHashMap<String, Person> loadCollection(String filename)
            throws IOException {

        Path path = Path.of(filename);
        if (!Files.exists(path)) {
            return new LinkedHashMap<>();
        }
        String json = Files.readString(path, StandardCharsets.UTF_8);
        Type type = new TypeToken<LinkedHashMap<String, Person>>() {}.getType();
        LinkedHashMap<String, Person> collection = gson.fromJson(json, type);
        return collection != null ? collection : new LinkedHashMap<>();
    }
}