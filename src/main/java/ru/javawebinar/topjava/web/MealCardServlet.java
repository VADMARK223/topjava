package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealRepository;
import ru.javawebinar.topjava.service.TopjavaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

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
        log.debug("Redirect meal card.");
        resp.sendRedirect("meal-card.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String datetime = req.getParameter("datetime");
        String description = req.getParameter("description");
        String calories = req.getParameter("calories");

        log.debug("Post in meal card: datetime={}, description={}, calories={}. ", datetime, description, calories);

        System.out.println("mealRepository.findAll().size(): " + mealRepository.findAll().size());

        Optional<Meal> mealOptional = mealRepository.findById(2L);
        if (mealOptional.isPresent()) {
            Meal meal = mealOptional.get();
            meal.setDescription(description);
            mealRepository.save(meal);
        } else {
            mealRepository.save(new Meal(99L, LocalDateTime.now(), "New", 123));
        }

        resp.sendRedirect("meals");
    }


}
