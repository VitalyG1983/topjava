package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MemoryMealStorage;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static MealStorage mealStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealStorage = new MemoryMealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> MealToList = MealsUtil.filteredByStreams(mealStorage.getAll(),
                    LocalTime.of(0, 0, 0, 0),
                    LocalTime.of(23, 59, 59, 999999999), MealsUtil.CALORIES_PER_DAY);
            request.setAttribute("meals", MealToList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }
        Meal m;
        switch (action) {
            case "delete":
                mealStorage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                return;
            case "edit":
                m = mealStorage.get(Integer.parseInt(id));
                request.setAttribute("newMeal", false);
                break;
            case "newMeal":
                m = new Meal(null);
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
        mealStorage.save(new Meal(newMeal ? null : Integer.parseInt(id), dateTime, description, Integer.parseInt(calories)));

        response.sendRedirect("meals");
    }
}