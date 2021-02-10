package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealCrudRepository;
import ru.javawebinar.topjava.service.CrudRepository;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

/**
 * @author Markitanov Vadim
 * @since 06.02.2021
 */
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final CrudRepository<Meal, Long> mealRepository = new MealCrudRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Redirect to meals.");
        req.setAttribute("meals", filteredByStreams(mealRepository.findAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        req.setAttribute("formatter", TimeUtil.formatter);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
