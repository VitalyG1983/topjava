package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Comparator<Meal> MEAL_DATE_TIME_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    {
        for (Meal meal : MealsUtil.meals) {
            save(meal, meal.getUserId());
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> oldMeal.getUserId() == userId ? meal : oldMeal);
    }

    @Override
    public boolean delete(int id, int userId) {
        AtomicBoolean deleted = new AtomicBoolean();
        repository.computeIfPresent(id, (key, oldValue) -> {
            deleted.set(oldValue.getUserId() == userId);
            return deleted.get() ? null : oldValue;
        });
        return deleted.get();
    }

    @Override
    public Meal get(int id, int userId) {
        Meal m = repository.get(id);
        return m != null && (m.getUserId() == userId) ? m : null;
    }

    @Override
    public List<Meal> getAllForUser(int userId) {
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .sorted(MEAL_DATE_TIME_COMPARATOR)
                .collect(Collectors.toList());
    }
}