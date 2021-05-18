package ru.javawebinar.topjava.repository.meal;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MapRepository implements Repository {

    private final Map<Long, Meal> storage = new ConcurrentHashMap<>();

    private final AtomicLong counter = new AtomicLong(0);

    private long getCounter() {
        return counter.incrementAndGet();
    }

    @Override
    public void save(Meal meal) {
        long id = getCounter();
        meal.setId(id);
        storage.putIfAbsent(id, meal);
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
}
