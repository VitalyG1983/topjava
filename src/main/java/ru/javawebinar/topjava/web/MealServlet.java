package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController MealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        log.info("Bean definition names: {}", Arrays.toString(appCtx.getBeanDefinitionNames()));
        MealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        switch (action != null ? action : "all") {
            case "createupdate":
                String id = request.getParameter("id");
                Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                        LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")));
                log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
                if (meal.isNew()) {
                    MealRestController.create(meal);
                } else {
                    MealRestController.update(meal, Integer.parseInt(id));
                }
                break;
            case "all":
            default:
                break;
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                MealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        MealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                log.info("Filter meals table");
                String startDateStr = request.getParameter("startDate");
                LocalDate startDate = !isBlank(startDateStr) ? LocalDate.parse(startDateStr) : null;
                String endDateStr = request.getParameter("endDate");
                LocalDate endDate = !isBlank(endDateStr) ? LocalDate.parse(endDateStr) : null;
                String startTimeStr = request.getParameter("startTime");
                LocalTime startTime = !isBlank(startTimeStr) ? LocalTime.parse(startTimeStr) : null;
                String endTimeStr = request.getParameter("endTime");
                LocalTime endTime = !isBlank(endTimeStr) ? LocalTime.parse(endTimeStr) : null;
                request.setAttribute("meals", MealRestController.getAllFiltered(startDate, endDate, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll() meals for selected User");
                request.setAttribute("meals",
                        MealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}