package ru.javawebinar.topjava.storage.meal;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapStorage implements Storage {

    private final Map<Long, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void save(Meal meal) {
        storage.putIfAbsent(meal.getId(), meal);
    }

    @Override
    public Meal get(long id) {
        return storage.get(id);
    }

    @Override
    public void delete(long id) {
        storage.remove(id);
    }

    @Override
    public void update(Meal meal) {
        storage.replace(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
