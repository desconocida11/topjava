package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.meal.MapRepository;
import ru.javawebinar.topjava.repository.meal.Repository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final Repository mealRepository = new MapRepository();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        for (Meal meal : MEALS_INIT) {
            mealRepository.save(meal);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        if (action == null) {
            List<MealTo> mealTo = filteredByStreams(mealRepository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("meals", mealTo);
            request.getRequestDispatcher("/WEB-INF/meals.jsp").forward(request, response);
            return;
        }
        long parseId = id == null ? 0 : Long.parseLong(id);
        switch (action) {
            case "delete":
                if (parseId == 0) {
                    log.error("id=0 on delete");
                }
                mealRepository.delete(parseId);
                response.sendRedirect("meals");
                return;
            case "update":
                Meal meal = parseId == 0 ? new Meal() : mealRepository.get(parseId);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/WEB-INF/update.jsp").forward(request, response);
                return;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String id = request.getParameter("id");
        long parseId = id == null ? 0 : Long.parseLong(id);
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("datetime")), request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (parseId != 0) {
            meal.setId(parseId);
            mealRepository.update(meal);
        } else {
            mealRepository.save(meal);
        }
        response.sendRedirect("meals");
    }
}
