package ru.javawebinar.topjava.repository.meal;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Repository {
    void save(Meal meal);

    Meal get(long id);

    void delete(long id);

    void update(Meal meal);

    List<Meal> getAll();
}
