package server;

import common.models.Person;
import common.util.JsonSerializer;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

public class CollectionManager {
    private LinkedHashMap<String, Person> collection;
    private final LocalDateTime initializationDate;
    private final String filename;

    public CollectionManager() {
        this.collection = new LinkedHashMap<>();
        this.initializationDate = LocalDateTime.now();
        this.filename = System.getenv("COLLECTION_FILE");
    }

    /**
     * Валидирует ID у всех объектов в коллекции.
     * Присваивает новые уникальные ID элементам с некорректными или дублирующимися значениями.
     */
    public void validateIDs() {
        if (collection.isEmpty()) {
            return;
        }

        Set<Integer> usedIds = new HashSet<>();
        int nextId = 1;

        for (Person person : collection.values()) {
            if (person.getId() <= 0 || !usedIds.add(person.getId())) {
                while (usedIds.contains(nextId)) {
                    nextId++;
                    if (nextId == Integer.MAX_VALUE) {
                        throw new RuntimeException("Достигнут предел ID");
                    }
                }
                person.setId(nextId);
                usedIds.add(nextId);
            }
            if (person.getId() < Integer.MAX_VALUE) {
                nextId = Math.max(nextId, person.getId() + 1);
            }
        }
    }

    /**
     * Генерирует следующий свободный ID.
     */
    private int generateNextID() {
        if (collection.isEmpty()) {
            return 1;
        }

        List<Integer> existingIds = collection.values().stream()
                .map(Person::getId)
                .sorted()
                .toList();

        int expectedId = 1;
        for (int id : existingIds) {
            if (id != expectedId) {
                return expectedId;
            }
            expectedId++;
        }

        int maxId = existingIds.get(existingIds.size() - 1);
        if (maxId == Integer.MAX_VALUE) {
            throw new RuntimeException("Достигнут предел ID");
        }
        return maxId + 1;
    }
    /**
     * Добавляет объект Person в коллекцию.
     */
    public boolean add(Person person) {
        if (person == null) {
            return false;
        }

        try {
            int newID = generateNextID();
            person.setId(newID);

            String key = String.valueOf(newID);
            if (collection.containsKey(key)) {
                return false;
            }

            collection.put(key, person);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void loadFromFile() {
        if (filename == null || filename.isEmpty()) {
            System.err.println("Переменная окружения не установлена");
            return;
        }

        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Файл не найден. Создаётся новая коллекция.");
            return;
        }

        try {
            collection = JsonSerializer.loadCollection(filename);
            validateIDs();
            System.out.println("Коллекция загружена из " + filename);
            System.out.println("В коллекции " + collection.size() + " элементов");

        } catch (Exception e) {
            System.err.println(e.getMessage());
            collection = new LinkedHashMap<>();
        }
    }

    public void saveToFile() {
        if (filename == null || filename.isEmpty()) {
            System.err.println("Переменная окружения не установлена");
            return;
        }

        try {
            JsonSerializer.saveCollection(collection, filename);
            System.out.println("Коллекция сохранена в " + filename);
            System.out.println("В коллекции " + collection.size() + " элементов");

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public LinkedHashMap<String, Person> getCollection() {
        return collection;
    }

    public int size() {
        return collection.size();
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public boolean removeByKey(String key) {
        return collection.remove(key) != null;
    }

    public void clear() {
        collection.clear();
    }
}