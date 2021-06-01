package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "adminName", "email@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.USER));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            MealsUtil.meals.forEach(mealRestController::create);
            List<MealTo> all =
                    mealRestController.getFiltered(LocalDate.of(2020, 1, 31), LocalTime.of(13, 0), null, null);
            if (all != null) {
                all.forEach(System.out::println);
            }
            mealRestController.getAll().forEach(System.out::println);
        }
    }
}
