package server;

import common.models.Person;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CollectionManager {
    private final ConcurrentHashMap<String, Person> collection;
    private final LocalDateTime initializationDate;
    private final PersonDAO personDAO;

    public CollectionManager(PersonDAO personDAO) {
        this.collection = new ConcurrentHashMap<>();
        this.initializationDate = LocalDateTime.now();
        this.personDAO = personDAO;
    }

    public void loadFromDatabase() throws SQLException {
        collection.clear();

        List<Person> persons = personDAO.loadAll();

        for (Person p : persons) {
            collection.put(String.valueOf(p.getId()), p);
        }

        System.out.println("Загружено из базы данных: " + persons.size());
    }

    public ConcurrentHashMap<String, Person> getCollection() {
        return collection;
    }

    public int size() {
        return collection.size();
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }
}