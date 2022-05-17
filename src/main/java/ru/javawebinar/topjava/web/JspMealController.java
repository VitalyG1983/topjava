package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    private static final Logger log = LoggerFactory.getLogger(ru.javawebinar.topjava.web.JspMealController.class);

    @Autowired
    private MealService service;

    @GetMapping
    public String getMeals(Model model) {
        log.info("getMeals");
        //int userId = Integer.parseInt(request.getParameter("userId"));
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @GetMapping(params = {"action"})
    public String getForSave(Model model, @RequestParam(required = false) Integer id) {
        log.info("getForSave");
        final Meal meal = id == null ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                service.get(id, SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping(params = {"id", "action=delete"})
    public String deleteMeal(Integer id) {
        log.info("deleteMeal");
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:meals";
    }

    @GetMapping(params = {"startDate", "endDate", "startTime", "endTime"})
    public String filterMeals(Model model, HttpServletRequest request) {
        log.info("filterMeals");
        List<Meal> mealsDateFiltered = service.getBetweenInclusive(parseLocalDate(request.getParameter("startDate")),
                parseLocalDate(request.getParameter("endDate")), SecurityUtil.authUserId());
        List<MealTo> mealTo = MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(),
                parseLocalTime(request.getParameter("startTime")), parseLocalTime(request.getParameter("endTime")));
        model.addAttribute("meals", mealTo);
        return "meals";
    }

    @PostMapping
    public String saveMeal(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int userId = SecurityUtil.authUserId();
        if (StringUtils.hasLength(request.getParameter("id"))) {
            // mealController.update(meal, getId(request));
            assureIdConsistent(meal, getId(request));
            log.info("update {} for user {}", meal, userId);
            service.update(meal, userId);
        } else {
            // mealController.create(meal);
            checkNew(meal);
            log.info("create {} for user {}", meal, userId);
            service.create(meal, userId);
        }
        return "redirect:meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}