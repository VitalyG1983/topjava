package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Comparator<Meal> MEAL_DATE_TIME_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    {
        for (Meal meal : MealsUtil.meals) {
            save(meal, meal.getDate().getDayOfMonth() == 30 ? 1 : 2);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return (fitUserId(meal.getUserId(), userId)) ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal m = repository.computeIfPresent(id, (key, oldValue) -> {
            if (fitUserId(oldValue.getUserId(), userId)) return null;
            return oldValue;
        });
        return m == null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal m = repository.get(id);
        return (m != null && fitUserId(m.getUserId(), userId)) ? m : null;
    }

    public List<Meal> getAll() {
        List<Meal> list = new ArrayList<>(repository.values());
        list.sort(MEAL_DATE_TIME_COMPARATOR);
        return list;
    }

    public Collection<Meal> getAllForUser(int userId) {
        return new ArrayList<>(repository.values()).stream().filter(m -> fitUserId(m.getUserId(), userId))
                .sorted(MEAL_DATE_TIME_COMPARATOR).collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllForUserFiltered(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        List<Meal> list = new ArrayList<>(repository.values()).stream().filter(m -> fitUserId(m.getUserId(), userId)
                        && DateTimeUtil.isBetweenHalfOpen(m.getDate(), startDate, endDate)
                        && DateTimeUtil.isBetweenHalfOpen(m.getTime(), startTime, endTime))
                .sorted(MEAL_DATE_TIME_COMPARATOR).collect(Collectors.toList());
        return list;
    }

     boolean fitUserId(Integer mealUserId, int actualUserId) {
        return mealUserId.equals(actualUserId);
    }
}