package modules;

import models.Person;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для управления коллекцией объектов Person.
 * Обрабатывает добавление, удаление, поиск и валидацию элементов.
 * @author kifmu
 */
public class CollectionHandler {

    private LinkedHashMap<String, Person> collection;
    private final LocalDateTime initializationDate;

    public CollectionHandler() {
        this.collection = new LinkedHashMap<>();
        this.initializationDate = LocalDateTime.now();
    }

    /**
     * Валидирует ID у всех объектов в коллекции.
     * Присваивает новые уникальные ID элементам с некорректными или дублирующимися значениями.
     * Начинает с 1, пропуская уже занятые ID.
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
                        System.err.println("Коллекция переполнена (ID=" + nextId + ")");
                        throw new RuntimeException("Достигнут предел ID. Очистите коллекцию.");
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
     * Генерирует следующий свободный ID для нового элемента.
     * @return первый незанятый ID, начиная с 1
     * @throws RuntimeException если достигнут предел Integer.MAX_VALUE
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
            throw new RuntimeException(
                    "Достигнут максимальный предел ID (2147483647). " +
                            "Очистите коллекцию или удалите элементы."
            );
        }

        return maxId + 1;
    }

    /**
     * Добавляет объект Person в коллекцию.
     * Автоматически присваивает новый ID и дату создания.
     * Если ключ не указан или равен "null", используется сгенерированный ID как ключ.
     * @param key ключ для элемента (может быть null)
     * @param person добавляемый объект
     * @return true если элемент успешно добавлен, false в случае ошибки
     */
    public boolean addToCollection(String key, Person person) {
        if (person == null) {
            System.err.println("Нельзя добавить объект null в коллекцию");
            return false;
        }
        try {
            int newID = generateNextID();
            person.setId(newID);
            person.setCreationDate(new Date());

            String finalKey;
            if (key == null || Objects.equals(key, "null")) {
                finalKey = String.valueOf(newID);
            } else {
                finalKey = key;
            }

            if (collection.containsKey(finalKey)) {
                System.err.println("Элемент с ключом '" + finalKey + "' уже существует");
                return false;
            }

            collection.put(finalKey, person);
            System.out.println("Ключ: '" + finalKey + "', ID: " + newID);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    /**
     * Проверяет наличие элемента по ключу.
     * @param key ключ для поиска
     * @return true если элемент существует, false иначе
     */
    public boolean checkExist(String key) {
        return collection.containsKey(key);
    }
    /**
     * Очищает коллекцию от всех элементов.
     */
    public void clearCollection() {
        collection.clear();
    }
    /**
     * Возвращает количество элементов в коллекции.
     * @return размер коллекции
     */
    public int size() {
        return collection.size();
    }

    /**
     * Устанавливает новую коллекцию.
     * @param collection новая коллекция LinkedHashMap.
     */
    public void setCollection(LinkedHashMap<String, Person> collection) {
        if (collection != null) {
            this.collection = collection;
        }
    }
    /**
     * Возвращает текущую коллекцию.
     * @return Коллекция с элементами.
     */
    public LinkedHashMap<String, Person> getCollection() {
        return collection;
    }
    /**
     * Возвращает дату и время инициализации.
     * @return LocalDateTime момента создания
     */
    public LocalDateTime getTime() {
        return initializationDate;
    }
    /**
     * Возвращает объект Person по ключу.
     * @param key ключ для поиска
     * @return найденный объект
     */
    public Person getByKey(String key) {
        return collection.get(key);
    }
}