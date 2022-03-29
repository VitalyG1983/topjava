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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    public static final Logger log = getLogger(MealServlet.class);
    private MealStorage mealStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealStorage = new MemoryMealStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Redirected to meals");
        String action = request.getParameter("action");
        switch (action == null ? "meallist" : action) {
            case "delete": {
                String id = Objects.requireNonNull(request.getParameter("id"));
                mealStorage.delete(Integer.parseInt(id));
                log.info("Redirect after \"delete\" id ={}to meals.jsp from MealServlet.doGet()", id);
                response.sendRedirect("meals");
                return;
            }
            case "newmeal":
            case "edit":
                Meal m;
                String id = null;
                if (action.equals("edit")) {
                    id = Objects.requireNonNull(request.getParameter("id"));
                    m = mealStorage.get(Integer.parseInt(id));
                } else {
                    m = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "newMeal", 0);
                }
                request.setAttribute("meal", m);
                log.info("Forward to editMeal.jsp with id={}", id);
                request.getRequestDispatcher("editMeal.jsp").forward(request, response);
                break;
            case "meallist":
            default:
                List<MealTo> mealToList = MealsUtil.filteredByStreams(mealStorage.getAll(),
                        LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
                request.setAttribute("meals", mealToList);
                log.info("Forward to meals.jsp from MealServlet.doGet()");
                request.getRequestDispatcher("meals.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = Objects.requireNonNull(request.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description").trim();
        int calories = Integer.parseInt(request.getParameter("calories").trim());
        if (id.isEmpty()) {
            mealStorage.save(new Meal(dateTime, description, calories));
        } else {
            mealStorage.save(new Meal(Integer.parseInt(id), dateTime, description, calories));
        }
        log.info("Redirect to meals.jsp from MealServlet.doPost() after \"save\" meal");
        response.sendRedirect("meals");
    }
}