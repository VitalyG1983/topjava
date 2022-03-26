package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.web.MealServlet.log;

public class MemoryMealStorage implements MealStorage {
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    public List<Meal> getAll() {
        log.info("getAll()");
        return new ArrayList<>(storage.values());
    }

    public Meal save(Meal m) {
        Integer oldId = m.getId();
        Integer newId = oldId != null ? oldId : idCounter.getAndIncrement();
        m.setId(newId);
        Meal meal = storage.put(newId, m);
        if (oldId != null) {
            log.info("Meal with newId={}  updated", oldId);
        } else {
            log.info("Meal with newId={} saved", newId);
        }
        return meal;
    }

    public boolean delete(int id) {
        log.info("Delete" + id);
        Meal meal;
        meal = storage.remove(id);
        if (meal != null) {
            log.info("Meal with id={} deleted", id);
        } else {
            log.info("Meal with id={} was not have mapping value", id);
        }
        return meal != null;
    }

    public Meal get(int id) {
        log.info("Get id={}", id);
        Meal meal;
        meal = storage.getOrDefault(id, null);
        if (meal != null) {
            log.info("Meal with id={} was gotten", id);
        } else {
            log.info("Meal with id={} have no mapping value", id);
        }
        return meal;
    }
}