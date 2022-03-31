package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private static final Comparator<Meal> MEAL_DATE_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (!fitUserId(meal.getUserId())) return null;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        Meal m = repository.computeIfPresent(id, (key, oldValue) -> {
            if (fitUserId(oldValue.getUserId())) return null;
            return oldValue;
        });
        return m == null;
    }

    @Override
    public Meal get(int id) {
        Meal m = repository.get(id);
        if (m != null && fitUserId(m.getUserId())) return m;
        else return null;
    }

    @Override
    public Collection<Meal> getAll(int userId, LocalDate start, LocalDate end) {
        return new ArrayList<>(repository.values()).stream()
                .filter(m -> fitUserId(m.getUserId()) && DateTimeUtil.isBetweenHalfOpen(m.getDate(), start, end))
                .sorted(MEAL_DATE_COMPARATOR).collect(Collectors.toList());
    }
}