package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.CrudRepository;
import ru.javawebinar.topjava.service.MealInMemoryCrudRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

/**
 * @author Markitanov Vadim
 * @since 06.02.2021
 */
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private CrudRepository<Meal, Long> mealRepository;

    @Override
    public void init() {
        log.debug("Init.");
        mealRepository = new MealInMemoryCrudRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String action = req.getParameter("action");
        log.debug("Get({}) meals.", action);
        if ("card".equals(action)) {
            Long cardId = req.getParameter("id") != null ? Long.parseLong(req.getParameter("id")) : null;
            log.debug("Redirect meal card: id={}.", cardId);
            Meal meal = mealRepository.findById(cardId).orElse(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "New", 0));
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("/meal-card.jsp").forward(req, resp);
        } else {
            req.setAttribute("meals", filteredByStreams(mealRepository.findAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getCharacterEncoding() == null) {
            req.setCharacterEncoding("UTF-8");
        }

        final String action = req.getParameter("action");
        log.debug("Post({}) in meals.", action);
        if ("delete".equals(action)) {
            long id = Long.parseLong(req.getParameter("id"));
            log.debug("Remove meal({}) result: {}.", id, mealRepository.deleteById(id));
            resp.sendRedirect("meals");
        } else if (("add-meal").equals(action)) {
            Long id = req.getParameter("id").isEmpty() ? null : Long.parseLong(req.getParameter("id"));
            LocalDateTime datetime = LocalDateTime.parse(req.getParameter("datetime"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));

            log.debug("Post in meal card: id={}, datetime={}, description={}, calories={}. ", id, datetime, description, calories);

            if (id == null) {
                log.debug("Create meal result: {}.", mealRepository.create(new Meal(datetime, description, calories)));
            } else {
                mealRepository.update(new Meal(datetime, description, calories).setId(id));


            }

            resp.sendRedirect("meals");
        }
    }
}
