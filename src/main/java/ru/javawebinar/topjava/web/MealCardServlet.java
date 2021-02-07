package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealRepository;
import ru.javawebinar.topjava.service.TopjavaRepository;
import ru.javawebinar.topjava.util.IdUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Markitanov Vadim
 * @since 07.02.2021
 */
public class MealCardServlet extends HttpServlet {
    private static final Logger log = getLogger(MealCardServlet.class);
    private final TopjavaRepository<Meal, Long> mealRepository = new MealRepository();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long cardId = req.getParameter("id") != null ? Long.parseLong(req.getParameter("id")) : -1L;
        log.debug("Redirect meal card: id={}.", cardId);
        Meal meal = mealRepository.findById(cardId).orElse(new Meal(-1L, LocalDateTime.now(), "New", 0));
        req.setAttribute("meal", meal);
        req.getRequestDispatcher("/meal-card.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getCharacterEncoding() == null) {
            req.setCharacterEncoding("UTF-8");
        }

        long id = Long.parseLong(req.getParameter("id"));
        LocalDateTime datetime = LocalDateTime.parse(req.getParameter("datetime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        log.debug("Post in meal card: id={}, datetime={}, description={}, calories={}. ", id, datetime, description, calories);

        if (id == -1) {
            log.debug("Create new");
            Meal meal = new Meal(IdUtil.getId(), datetime, description, calories);
            mealRepository.save(meal);
        } else {
            log.debug("Update");
            mealRepository.findById(id).ifPresent(meal -> {
                meal.setDateTime(datetime);
                meal.setDescription(description);
                meal.setCalories(calories);
            });
        }

        resp.sendRedirect("meals");
    }


}
