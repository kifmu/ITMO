package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Person;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

/**
 * Класс для сериализации и десериализации объектов в формат JSON.
 * @author kifmu
 */
public class JsonUtil {
    private static final Gson gson;

    static {
        gson = new GsonBuilder().create();
    }

    /**
     * Сериализует коллекцию людей в JSON-строку.
     * @param collection коллекция LinkedHashMap, содержащая пары ключ-значение (ID-Person)
     * @return JSON-представление коллекции
     */
    public static String serializeCollection(LinkedHashMap<String, Person> collection) {
        return gson.toJson(collection);
    }

    /**
     * Десериализует JSON-строку в объект Person.
     * @param json строка в формате JSON
     * @return объект Person
     */
    public static Person deserializePerson(String json) {
        return gson.fromJson(json, Person.class);
    }

    /**
     * Десериализует JSON-строку в коллекцию LinkedHashMap с объектами Person.
     * @param json строка в формате JSON
     * @return коллекция LinkedHashMap, заполненная данными из JSON
     */
    public static LinkedHashMap<String, Person> deserializeCollection(String json) {
        Type type = new com.google.gson.reflect.TypeToken<LinkedHashMap<String, Person>>() {}.getType();
        return gson.fromJson(json, type);
    }
}