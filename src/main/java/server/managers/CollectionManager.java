package server.managers;

import global.models.Route;
import java.time.LocalDateTime;
import java.util.*;
import server.tools.CSVparser;

/**
 * Менеджер коллекции
 */
public class CollectionManager {
    private Integer currentId = 1;
    private final Map<Integer, Route> routes = new HashMap<>();
    private final List<Route> collection = new LinkedList<>();
    private final CSVparser csvparser;
    private LocalDateTime lastSaveTime;
    private LocalDateTime lastInitTime;

    public CollectionManager(CSVparser csvparser) {
        this.lastSaveTime = null;
        this.csvparser = csvparser;

        update();
    }

    public LocalDateTime getLastSaveTime() { return lastSaveTime; }

    /**
     * @return коллекция
     */
    public List<Route> getCollection() {
        return collection;
    }

    /**
     * @param id id Route
     * @return Route по id
     */
    public Route byId(Integer id) {
        return routes.get(id);
    }

    /**
     * Добавляет Route
     * @param route маршрут
     */
    public void add(Route route) {
        collection.add(route);
        routes.put(route.getId(), route);
        update();
    }

    /**
     * Удаляет Route по id
     */
    public void remove(Integer id) {
        var route = byId(id);
        if (route == null) return;
        routes.remove(route.getId());
        collection.remove(route);
        update();
    }

    /**
     * Сортирует коллекцию
     */
    public void update() {
        Collections.sort(collection);
    }

    public void clear() {
        collection.clear();
        routes.clear();
    }

    public void saveCollection() {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста");
        }
        csvparser.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }


    public boolean loadCollection() {
        routes.clear();
        csvparser.readCollection(collection);
        lastInitTime = LocalDateTime.now();
        for (var e : collection)
            if (byId(e.getId()) != null) {
                collection.clear();
                return false;
            } else {
                if (e.getId() > currentId) currentId = e.getId();
                routes.put(e.getId(), e);
            }
        update();
        return true;
    }


    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста";
        StringBuilder info = new StringBuilder();
        for (var route : collection) {
            info.append(route).append("\n");
        }
        return info.toString().trim();
    }
}