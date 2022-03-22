package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;

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

//        request.getRequestDispatcher("/users.jsp").forward(request, response);
        //      response.sendRedirect("meals.jsp");

        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("meals", storage.getAllSorted());
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }
        Meal m;
        switch (action) {
            case "delete":
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "edit":
                m = storage.get(id);
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
}
