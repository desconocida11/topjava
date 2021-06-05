package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL = START_SEQ + 2;
    public static final int ADMIN_MEAL = START_SEQ + 3;
    public static final int NOT_FOUND = 10;

    public static final Meal userMeal = new Meal(USER_MEAL, LocalDateTime.of(2021, 6, 5, 15, 0, 0),
            "user lunch", 1000);
    public static final Meal adminMeal = new Meal(ADMIN_MEAL, LocalDateTime.of(2021, 6, 5, 16, 0, 0),
            "admin lunch", 1200);


    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2021, 6, 6, 10, 0, 0, 0), "new meal", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal);
        updated.setDateTime(LocalDateTime.of(2021, 6, 5, 13, 0, 0, 0));
        updated.setDescription("updated user lunch");
        updated.setCalories(1500);
        return updated;
    }

    public static void assertMealMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMealMatch(Iterable<Meal> actual, Meal... expected) {
        assertMealMatch(actual, Arrays.asList(expected));
    }

    public static void assertMealMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
