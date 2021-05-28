package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, Integer userId) {
        Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, k -> new HashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealMap.replace(meal.getId(), meal);
    }

    @Override
    public synchronized boolean delete(int id, Integer userId) {
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        return repository.get(userId).get(id);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository.getOrDefault(userId, new HashMap<>()).values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}

