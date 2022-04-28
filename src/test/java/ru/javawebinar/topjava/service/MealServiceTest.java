package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TestTimeWatcher;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.getNew;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.util.TestTimeWatcher.testTimeArray;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @AfterClass
    public static void afterClass() {
        System.out.println("\nMealServiceTest.class tests complete, time spent for tests:");
        testTimeArray.forEach(System.out::println);
        testTimeArray.clear();
    }

    @Rule
    public final TestRule watchMealTest = new TestTimeWatcher();

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(user), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew(user);
        newMeal.setId(newId);
        MEAL_MATCHER_IGNORING.assertMatch(created, newMeal);
        MEAL_MATCHER_IGNORING.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER_IGNORING.assertMatch(actual, adminMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated(user);
        service.update(updated, USER_ID);
        MEAL_MATCHER_IGNORING.assertMatch(service.get(MEAL1_ID, USER_ID), getUpdated(user));
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
        MEAL_MATCHER_IGNORING.assertMatch(service.get(MEAL1_ID, USER_ID), meal1);
    }

    @Test
    public void getAll() {
        MEAL_MATCHER_IGNORING.assertMatch(service.getAll(USER_ID), meals);
    }

    @Test
    public void getBetweenInclusive() {
        MEAL_MATCHER_IGNORING.assertMatch(service.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void getBetweenWithNullDates() {
        MEAL_MATCHER_IGNORING.assertMatch(service.getBetweenInclusive(null, null, USER_ID), meals);
    }
}