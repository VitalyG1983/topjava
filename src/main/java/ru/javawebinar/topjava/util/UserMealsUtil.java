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

        List<UserMealWithExcess> mealsRecursion = filteredByCyclesRecursion(meals, LocalTime.of(7, 0),
                LocalTime.of(23, 0), 2000);
        mealsRecursion.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(23, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        Map<LocalDate, Integer> caloriesDayMap = new HashMap<>();
        for (UserMeal meal : meals) {
            int mealCalories = meal.getCalories();
            LocalDateTime mealDateTime = meal.getDateTime();
            caloriesDayMap.merge(mealDateTime.toLocalDate(), mealCalories, Integer::sum);
        }
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcessList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), caloriesDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalories = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        dayCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCyclesRecursion(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> caloriesDayMap = new HashMap<>();
        Recursion(new ArrayList<>(meals), userMealWithExcess, caloriesDayMap, startTime, endTime, caloriesPerDay);
        return userMealWithExcess;
    }

    public static void Recursion(List<UserMeal> meals, List<UserMealWithExcess> userMealWithExcessList, Map<LocalDate, Integer> caloriesDayMap,
                                 LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (meals.size() == 0)
            return;
        UserMeal meal = meals.remove(0);
        int mealCalories = meal.getCalories();
        LocalDateTime mealDateTime = meal.getDateTime();
        LocalDate mealDate = mealDateTime.toLocalDate();
        caloriesDayMap.merge(mealDate, mealCalories, Integer::sum);
        Recursion(meals, userMealWithExcessList, caloriesDayMap, startTime, endTime, caloriesPerDay);
        if (TimeUtil.isBetweenHalfOpen(mealDateTime.toLocalTime(), startTime, endTime)) {
            userMealWithExcessList.add(new UserMealWithExcess(mealDateTime, meal.getDescription(),
                    meal.getCalories(), caloriesDayMap.get(mealDate) > caloriesPerDay));
        }
    }
}