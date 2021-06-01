package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealRepository {
    // null if updated meal do not belong to userId
    Meal save(Meal meal, Integer userId);

    // false if meal do not belong to userId
    boolean delete(int id, Integer userId);

    // null if meal do not belong to userId
    Meal get(int id, Integer userId);

    // ORDERED dateTime desc
    Collection<Meal> getAll(Integer userId);

    // ORDERED dateTime desc, filtered by date
    Collection<Meal> getAllFiltered(Integer userId, LocalDate startDate, LocalDate endDate);
}
