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
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    public static final Logger log = getLogger(MealServlet.class);
    private static MealStorage mealStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealStorage = new MemoryMealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String id = null;
        String action = request.getParameter("action");
        if (action != null && !action.equals("newMeal")) {
            id = Objects.requireNonNull(request.getParameter("id"));
        }
        Meal m;
        switch (action == null ? "mealList" : action) {
            case "delete":
                mealStorage.delete(Integer.parseInt(id));
                log.info("Redirect after \"delete\" id ={}to meals.jsp from MealServlet.doGet()", id);
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
            case "mealList":
            default:
                List<MealTo> MealToList = MealsUtil.filteredByStreams(mealStorage.getAll(),
                        LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
                request.setAttribute("meals", MealToList);
                log.info("Forward with unknown 'action' to meals.jsp from MealServlet.doGet()");
                request.getRequestDispatcher("meals.jsp").forward(request, response);
                return;
        }
        request.setAttribute("meal", m);
        log.info("Forward to edit.jsp with IdMeal={}", id);
        request.getRequestDispatcher("edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = Objects.requireNonNull(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description").trim();
        int calories = Integer.parseInt(request.getParameter("calories").trim());
        boolean newMeal = request.getParameter("newMeal").equals("true");
        mealStorage.save(new Meal(newMeal ? null : Integer.parseInt(id), dateTime, description, calories));
        log.info("Redirect to meals.jsp from MealServlet.doPost() after \"save\" meal");
        response.sendRedirect("meals");
    }
}