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
                (localDateMap, meal) -> {
                    LocalDate keyLocalDate = meal.getDateTime().toLocalDate();
                    localDateMap.computeIfAbsent(keyLocalDate, key -> new AbstractMap.SimpleEntry<>(new ArrayList<>(), new ArrayList<>()));
                    Map.Entry<List<Integer>, List<UserMeal>> caloriesAndMealsPerDay = localDateMap.get(keyLocalDate);
                    caloriesAndMealsPerDay.getKey().add(meal.getCalories());
                    if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                        caloriesAndMealsPerDay.getValue().add(meal);
                    }
                },
                (localDateMap, localDateMapComb) -> {
                    Map<LocalDate, Map.Entry<List<Integer>, List<UserMeal>>> result = new HashMap<>();
                    localDateMap.forEach((key, value) -> localDateMapComb.merge(key, value, (map1, map2) -> {
                        AbstractMap.SimpleEntry<List<Integer>, List<UserMeal>> entry = new AbstractMap.SimpleEntry<>(new ArrayList<>(), new ArrayList<>());
                        map1.getKey().forEach(map1Key -> entry.getKey().add(map1Key));
                        map1.getValue().forEach(map1Key -> entry.getValue().add(map1Key));
                        map2.getKey().forEach(map2Key -> entry.getKey().add(map2Key));
                        map2.getValue().forEach(map2Key -> entry.getValue().add(map2Key));
                        return entry;
                    }));
                    return result;
                },
                localDateMap -> {
                    List<UserMealWithExcess> result = new ArrayList<>();
                    for (Map.Entry<List<Integer>, List<UserMeal>> entry : localDateMap.values()) {
                        List<UserMeal> userMeals = entry.getValue();
                        if (!userMeals.isEmpty()) {
                            final boolean excess = entry.getKey().stream()
                                    .mapToInt(Integer::intValue)
                                    .sum() > caloriesPerDay;
                            userMeals.forEach((meal) -> result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess)));
                        }
                    }
                    return result;
                });
    }
}
