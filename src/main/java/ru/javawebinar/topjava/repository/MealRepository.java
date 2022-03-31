package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.Collection;

// TODO add userId
public interface MealRepository {
    // null if updated meal does not belong to userId
    Meal save(Meal meal);

    // false if meal does not belong to userId
    boolean delete(int id);

    // null if meal does not belong to userId
    Meal get(int id);

    // ORDERED dateTime desc
    Collection<Meal> getAll(int userId, LocalDate start, LocalDate end);

    default boolean fitUserId(Integer userId) {
        return userId.equals(SecurityUtil.authUserId());
    }
}