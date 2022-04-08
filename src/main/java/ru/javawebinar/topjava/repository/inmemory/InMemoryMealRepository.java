package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Comparator<Meal> MEAL_DATE_TIME_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    {
        for (Meal meal : MealsUtil.meals) {
            save(meal, meal.getUserId());
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, k -> new HashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        AtomicBoolean updated = new AtomicBoolean();
        mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (oldMeal.getUserId() == userId) {
                updated.set(true);
                meal.setUserId(userId);
            }
            return updated.get() ? meal : oldMeal;
        });
        return updated.get() ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            return false;
        }
        AtomicBoolean deleted = new AtomicBoolean();
        mealMap.computeIfPresent(id, (key, oldValue) -> {
            deleted.set(oldValue.getUserId() == userId);
            return deleted.get() ? null : oldValue;
        });
        return deleted.get();
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            return null;
        }
        Meal m = mealMap.get(id);
        return m != null && (m.getUserId() == userId) ? m : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getMealList(userId, m -> true);
    }

    @Override
    public List<Meal> getAllFiltered(int userId, LocalDate start, LocalDate end) {
        return getMealList(userId, m -> DateTimeUtil.isBetweenDate(m.getDate(), start, end));
    }

    private List<Meal> getMealList(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null || mealMap.isEmpty() ? new ArrayList<>() : mealMap.values().stream()
                .filter(filter)
                .sorted(MEAL_DATE_TIME_COMPARATOR)
                .collect(Collectors.toList());
    }
}