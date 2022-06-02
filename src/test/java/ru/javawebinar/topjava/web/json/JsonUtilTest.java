package ru.javawebinar.topjava.web.json;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;
import static ru.javawebinar.topjava.UserTestData.adminWithMeals;

class JsonUtilTest {

    @Test
    void readWriteValue() {
        String json = JsonUtil.writeValue(adminMeal1);
        System.out.println(json);
        Meal meal = JsonUtil.readValue(json, Meal.class);
        MEAL_MATCHER.assertMatch(meal, adminMeal1);

        json = JsonUtil.writeValue(adminWithMeals);
        System.out.println(json);
        User user = JsonUtil.readValue(json, User.class);
        USER_MATCHER.assertMatch(user, adminWithMeals);
    }

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(meals);
        System.out.println(json);
        List<Meal> meals = JsonUtil.readValues(json, Meal.class);
        MEAL_MATCHER.assertMatch(meals, MealTestData.meals);
    }
}