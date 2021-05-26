package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public synchronized Meal save(Meal meal, Integer userId) {
        if (!meal.getUserId().equals(userId)) {
            return null;
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public synchronized boolean delete(int id, Integer userId) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId().equals(userId)) {
            return repository.remove(id, meal);
        }
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId().equals(userId)) {
            return meal;
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId().equals(userId))
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}

