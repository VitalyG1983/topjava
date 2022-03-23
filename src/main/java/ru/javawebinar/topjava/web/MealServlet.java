package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.AbstractStorage;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new MapMealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> MealToList = MealsUtil.filteredByStreams(storage.getAllSorted(),
                    null, null, AbstractStorage.CALORIES_PER_DAY);
            request.setAttribute("meals", MealToList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }
        Meal m;
        switch (action) {
            case "delete":
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                return;
            case "edit":
                m = storage.get(Integer.parseInt(id));
                request.setAttribute("newMeal", false);
                break;
            case "newMeal":
                m = new Meal();
                request.setAttribute("newMeal", true);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("meal", m);
        request.getRequestDispatcher("edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description").trim();
        String calories = request.getParameter("calories").trim();
        boolean newMeal = request.getParameter("newMeal").equals("true");
        if (!newMeal) {
            storage.delete(Integer.parseInt(id));
        }
        storage.save(new Meal(id, dateTime, description, Integer.parseInt(calories)));
        response.sendRedirect("meals");
    }
}