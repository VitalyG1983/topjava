package ru.javawebinar.topjava.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        MealRepoDataJpaTest.class,
        MealRepoJpaTest.class,
        MealRepoJdbcTest.class,
        UserRepoDataJpaTest.class,
        UserRepoJdbcTest.class,
        UserRepoJpaTest.class
})
public class AllRepositoriesTest {
}
