package ru.javawebinar.topjava.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealRepository;
import ru.javawebinar.topjava.service.TopjavaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
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
    private final TopjavaRepository<Meal, Long> mealRepository = new MealRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Redirect to meals.");
        req.setAttribute("meals", filteredByStreams(mealRepository.findAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception exception) {
            log.error("Error: " + exception.getMessage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        MealRequest request = null;
        try {
            request = objectMapper.readValue(stringBuilder.toString(), MealRequest.class);
            log.debug("Remove meal({}).", request.getId());
        } catch (Exception exception) {
            log.error("Error parse request: " + exception.getMessage());
        }

        if (request != null) {
            try {
                mealRepository.deleteById(request.getId());
            } catch (Exception exception) {
                exception.printStackTrace();
                log.error("Error meal delete: " + exception.getMessage());
            }
        }
    }
}
