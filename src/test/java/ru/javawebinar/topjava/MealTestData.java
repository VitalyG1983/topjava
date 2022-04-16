package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL1_ID = START_SEQ + 3;
    public static final int USER_MEAL2_ID = START_SEQ + 4;
    public static final int USER_MEAL3_ID = START_SEQ + 5;
    public static final int ADMIN_MEAL1_ID = START_SEQ + 6;
    public static final int ADMIN_MEAL2_ID = START_SEQ + 7;
    public static final int ADMIN_MEAL3_ID = START_SEQ + 8;
    public static final int NOT_FOUND = 100;

    public static final Meal userMeal1 = new Meal(USER_MEAL1_ID, LocalDateTime.of(2020, Month.JANUARY, 28, 10, 0), "User Завтрак", 501);
    public static final Meal userMeal2 = new Meal(USER_MEAL2_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "User Обед", 1000);
    public static final Meal userMeal3 = new Meal(USER_MEAL3_ID, LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "User Ужин", 500);
    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL1_ID, LocalDateTime.of(2020, Month.DECEMBER, 25, 10, 0), "Admin Завтрак", 1000);
    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL2_ID, LocalDateTime.of(2020, Month.DECEMBER, 31, 13, 0), "Admin Обед", 500);
    public static final Meal adminMeal3 = new Meal(ADMIN_MEAL3_ID, LocalDateTime.of(2020, Month.DECEMBER, 31, 20, 0), "Admin Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2012, 12, 12, 0, 0, 0), "new meal", 999);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal1);
        updated.setDateTime(LocalDateTime.of(2222, Month.JANUARY, 1, 1, 1));
        updated.setDescription("UpdatedDescription");
        updated.setCalories(222);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}