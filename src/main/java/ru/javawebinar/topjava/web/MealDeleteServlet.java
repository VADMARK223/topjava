package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealCrudRepository;
import ru.javawebinar.topjava.service.CrudRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Markitanov Vadim
 * @since 08.02.2021
 */
public class MealDeleteServlet extends HttpServlet {
    private static final Logger log = getLogger(MealDeleteServlet.class);
    private final CrudRepository<Meal, Long> mealRepository = new MealCrudRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        log.debug("Remove meal({}) result: {}.", id, mealRepository.deleteById(id));
        resp.sendRedirect("meals");
    }
}
