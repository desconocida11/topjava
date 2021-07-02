package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class MealController extends AbstractController {

    @Autowired
    private MealService mealService;

    @GetMapping
    public String getMeals(HttpServletRequest request, Model model) {
//        int authUserId = SecurityUtil.authUserId();
//        List<MealTo> meals = MealsUtil.getTos(mealService.getAll(authUserId), userService.get(authUserId).getCaloriesPerDay());
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<MealTo> meals = getBetween(startDate, startTime, endDate, endTime);
        model.addAttribute("meals", meals);
        return "meals";
    }

    @RequestMapping(value = "delete/{mealId}", method = RequestMethod.GET)
    public String deleteMeal(@PathVariable("mealId") int mealId) {
        mealService.delete(mealId, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @RequestMapping(value = "update/{mealId}", method = RequestMethod.GET)
    public String updateMeal(@PathVariable("mealId") int mealId, Model model) {
        Meal meal = mealService.get(mealId, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createMeal(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping
    public String saveMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Meal meal;
        int id = getId(request);
        if (id != 0) {
            meal = mealService.get(id, SecurityUtil.authUserId());
            meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
            meal.setDescription(request.getParameter("description"));
            meal.setCalories(Integer.parseInt(request.getParameter("calories")));
            mealService.update(meal, SecurityUtil.authUserId());
        } else {
            meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            mealService.create(meal, SecurityUtil.authUserId());
        }
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = request.getParameter("id");
        return paramId.isEmpty() ? 0 : Integer.parseInt(paramId);
    }

    private List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}
