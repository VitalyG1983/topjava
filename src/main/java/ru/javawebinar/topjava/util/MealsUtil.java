package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> meals = Arrays.asList(
            new Meal(null,1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "User Завтрак", 501),
            new Meal(null,1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "User Обед", 1000),
            new Meal(null,1,LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "User Ужин", 500),
            new Meal(null,1,LocalDateTime.of(2020, Month.JANUARY, 29, 9, 30), "User Завтрак", 200),
            new Meal(null,1,LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "User Ужин", 500),
            new Meal(null,2,LocalDateTime.of(2020, Month.DECEMBER, 31, 0, 0), "Admin Еда на граничное значение", 100),
            new Meal(null,2,LocalDateTime.of(2020, Month.DECEMBER, 31, 10, 0), "Admin Завтрак", 1000),
            new Meal(null,2,LocalDateTime.of(2020, Month.DECEMBER, 31, 13, 0), "Admin Обед", 500),
            new Meal(null,2,LocalDateTime.of(2020, Month.DECEMBER, 31, 20, 0), "Admin Ужин", 410),
            new Meal(null,2,LocalDateTime.of(2020, Month.DECEMBER, 25, 9, 0), "Admin Завтрак", 100),
            new Meal(null,2,LocalDateTime.of(2020, Month.DECEMBER, 25, 14, 0), "Admin Обед", 600)
    );

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
