package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    static final Comparator<UserMealWithExcess> USER_MEAL_WITH_EXCESS_COMPARATOR = Comparator.
            comparing(UserMealWithExcess::getDateTime);

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0),
                LocalTime.of(23, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(23, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> UserMealWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> caloriesDayMap = new HashMap<>();
        for (UserMeal meal : meals) {
            int mealCalories = meal.getCalories();
            LocalDateTime mealDateTime = meal.getDateTime();
            caloriesDayMap.merge(mealDateTime.toLocalDate(), mealCalories, Integer::sum);

            if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
                UserMealWithExcess.add(new UserMealWithExcess(mealDateTime, meal.getDescription(), mealCalories, false));
            }
        }
        for (UserMealWithExcess meal : UserMealWithExcess) {
            meal.setExcess(caloriesDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay);
        }
        UserMealWithExcess.sort(USER_MEAL_WITH_EXCESS_COMPARATOR);
        return UserMealWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        //  List<UserMealWithExcess> UserMealWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> dayCalories = new HashMap<>();
        List<UserMealWithExcess> UserMealWithExcessList = meals.stream()
                .map(meal -> {
                    int mealCalories = meal.getCalories();
                    dayCalories.merge(meal.getDateTime().toLocalDate(), mealCalories, Integer::sum);
                    return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), mealCalories, false);
                })
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList());
        return UserMealWithExcessList.stream()
                .peek(meal -> meal.setExcess(dayCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .sorted(USER_MEAL_WITH_EXCESS_COMPARATOR).collect(Collectors.toList());
    }

}