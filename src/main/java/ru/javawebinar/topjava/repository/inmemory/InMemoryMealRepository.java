package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
            save(meal, meal.getDate().getMonth() == Month.JANUARY ? 1 : 2);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            return false;
        }
        Meal m = mealMap.remove(id);
        return m != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            return null;
        }
        return mealMap.get(id);
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