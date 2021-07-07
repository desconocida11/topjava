package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.MealTo;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class MealController extends AbstractMealController {

    @Autowired
    protected UserService userService;

    @GetMapping
    public String getMeals(HttpServletRequest request, Model model) {
        List<MealTo> meals;
        String action = request.getParameter("action");
        if ("filter".equals(action)) {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            meals = getBetween(startDate, startTime, endDate, endTime);
        } else {
            meals = getAll();
        }
        model.addAttribute("meals", meals);
        return "meals";
    }

    @RequestMapping(value = "delete/{mealId}", method = RequestMethod.GET)
    public String deleteMeal(@PathVariable("mealId") int mealId) {
        delete(mealId);
        return "redirect:/meals";
    }

    @GetMapping(value = "update/{mealId}")
    public String updateMeal(@PathVariable("mealId") int mealId, Model model) {
        model.addAttribute("meal", get(mealId));
        return "mealForm";
    }

    @GetMapping(value = "create")
    public String createMeal(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping
    public String saveMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (StringUtils.hasLength(request.getParameter("id"))) {
            update(meal, getId(request));
        } else {
            create(meal);
        }
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
