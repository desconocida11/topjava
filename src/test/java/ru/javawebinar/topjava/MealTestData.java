package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL = START_SEQ + 2;
    public static final int ADMIN_MEAL = START_SEQ + 5;
    public static final int NOT_FOUND = 10;

    public static final List<Meal> userMeals;
    public static final List<Meal> adminMeals;

    static {
         userMeals = Arrays.asList(
                new Meal(USER_MEAL, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(USER_MEAL + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(USER_MEAL + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500)
         );
        adminMeals = Arrays.asList(
                new Meal(ADMIN_MEAL, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(ADMIN_MEAL + 1, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(ADMIN_MEAL + 2, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(ADMIN_MEAL + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
    }

    public static Meal getUserMeal() {
        return userMeals.get(0);
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2021, 6, 6, 10, 0, 0, 0), "new meal", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(getUserMeal());
        updated.setDateTime(LocalDateTime.of(2021, 6, 5, 13, 0, 0, 0));
        updated.setDescription("updated user lunch");
        updated.setCalories(1500);
        return updated;
    }

    public static void assertMealMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMealMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
