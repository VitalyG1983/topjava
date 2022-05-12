package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER_WITH_USER.assertMatch(actual, adminMeal1);
    }
}