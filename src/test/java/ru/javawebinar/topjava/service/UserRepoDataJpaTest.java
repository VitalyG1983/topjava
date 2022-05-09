package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles("datajpa")
public class UserRepoDataJpaTest extends UserServiceTest {

    @Test
    @Override
    public void get() {
        User u = service.get(USER_ID);
        USER_MATCHER_WITH_MEAL_LIST.assertMatch(u, user);
    }
}