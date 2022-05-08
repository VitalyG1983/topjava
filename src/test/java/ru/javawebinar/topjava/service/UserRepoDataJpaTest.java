package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER_WITH_MEAL_LIST;

@ActiveProfiles("datajpa")
public class UserRepoDataJpaTest extends UserServiceTest {

    @Test
    @Override
    public void get() {
        User user = service.get(USER_ID);
        USER_MATCHER_WITH_MEAL_LIST.assertMatch(user, UserTestData.getUserWithMeals());
    }
}