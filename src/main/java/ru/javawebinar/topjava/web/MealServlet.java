package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealRepository;
import ru.javawebinar.topjava.service.TopjavaRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Markitanov Vadim
 * @since 06.02.2021
 */
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final TopjavaRepository<MealTo, Long> mealRepository = new MealRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Redirect to meals.");
        req.setAttribute("meals", mealRepository.findAll());
        req.setAttribute("username", getServletContext().getInitParameter("username"));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Post meal: " + req.getParameter("id"));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long mealId = Long.valueOf(req.getParameter("id"));
        log.debug("Delete meal: " + mealId);
        mealRepository.deleteById(mealId);
    }
}
