package ru.javawebinar.topjava.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        DataJpaMealServiceTest.class,
        JpaMealServiceTest.class,
        JdbcMealServiceTest.class,
        DataJpaUserServiceTest.class,
        JdbcUserServiceTest.class,
        JpaUserServiceTest.class
})
public class AllRepositoriesTest {
}