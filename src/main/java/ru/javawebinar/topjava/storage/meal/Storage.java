package ru.javawebinar.topjava.storage.meal;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    void clear();

    void save(Meal meal);

    Meal get(long id);

    void delete(long id);

    void update(Meal meal);

    List<Meal> getAll();

    int size();
}
