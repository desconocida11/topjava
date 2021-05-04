package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;


public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);
        filteredByStreams_optional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate mealDate = meal.getDateTime().toLocalDate();
            int calories = meal.getCalories();
            dailyCalories.merge(mealDate, calories, Integer::sum);
        }
        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalDateTime mealDateTime = meal.getDateTime();
            if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                boolean excess = dailyCalories.get(mealDateTime.toLocalDate()) > caloriesPerDay;
                result.add(new UserMealWithExcess(mealDateTime, meal.getDescription(), meal.getCalories(), excess));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dailyCalories = meals.stream()
                .collect(groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    LocalDateTime mealDateTime = userMeal.getDateTime();
                    boolean excess = dailyCalories.get(mealDateTime.toLocalDate()) > caloriesPerDay;
                    return new UserMealWithExcess(mealDateTime, userMeal.getDescription(), userMeal.getCalories(), excess);
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreams_optional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(groupingByCalories(caloriesPerDay, startTime, endTime));
    }

    public static Collector<UserMeal, ?, List<UserMealWithExcess>> groupingByCalories(int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return Collector.<UserMeal, Map<LocalDate, Map.Entry<List<Integer>, List<UserMeal>>>, List<UserMealWithExcess>>of(
                HashMap::new,
                (c, e) -> {
                    LocalDate keyLocalDate = e.getDateTime().toLocalDate();
                    if (c.containsKey(keyLocalDate)) {
                        c.get(keyLocalDate).getKey().add(e.getCalories());
                        if (TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime)) {
                            c.get(keyLocalDate).getValue().add(e);
                        }
                    } else {
                        Map.Entry<List<Integer>, List<UserMeal>> value =
                                new AbstractMap.SimpleEntry<>(new ArrayList<>(), new ArrayList<>());
                        if (TimeUtil.isBetweenHalfOpen(e.getDateTime().toLocalTime(), startTime, endTime)) {
                            value.getValue().add(e);
                        }
                        value.getKey().add(e.getCalories());
                        c.put(keyLocalDate, value);
                    }
                },
                (c1, c2) -> {
                    c1.putAll(c2);
                    return c1;
                },
                c -> {
                    List<UserMealWithExcess> result = new ArrayList<>();
                    for (Map.Entry<LocalDate, Map.Entry<List<Integer>, List<UserMeal>>> entry : c.entrySet()) {
                        List<Integer> key = entry.getValue().getKey();
                        if (!key.isEmpty()) {
                            final boolean excess = key.stream()
                                    .mapToInt(Integer::intValue)
                                    .sum() > caloriesPerDay;
                            List<UserMeal> userMeals = entry.getValue().getValue();
                            userMeals.forEach((e) -> result.add(new UserMealWithExcess(e.getDateTime(), e.getDescription(), e.getCalories(), excess)));
                        }
                    }
                    return result;
                });
    }
}
