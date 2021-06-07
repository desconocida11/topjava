package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL, USER_ID);
        assertMealMatch(meal, getUserMeal());
    }

    @Test
    public void getNotAuth() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotAuth() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2020, 1, 31);
        LocalDate endDate = LocalDate.of(2020, 2, 1);
        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, ADMIN_ID);
        assertMealMatch(meals, adminMeals);
    }

    @Test
    public void duplicateDateTimeCreate() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2020, 1, 30, 13, 0, 0),
                "user second lunch", 500);
        assertThrows(DataAccessException.class, () -> service.create(newMeal, USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMealMatch(all, userMeals);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        assertMealMatch(service.get(USER_MEAL, USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateNotAuth() {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertMealMatch(created, newMeal);
        assertMealMatch(service.get(newId, USER_ID), newMeal);
    }
}