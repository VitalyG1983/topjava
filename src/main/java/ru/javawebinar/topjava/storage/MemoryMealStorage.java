package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MemoryMealStorage implements MealStorage {
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger();
    private static final Logger log = getLogger(MemoryMealStorage.class);

    {
        MealsUtil.meals.forEach(this::save);
    }

    public List<Meal> getAll() {
        log.info("getAll(): return meals values from Map storage");
        return new ArrayList<>(storage.values());
    }

    public Meal save(Meal m) {
        Integer oldId = m.getId();
        if (oldId == null) {
            int newId = idCounter.getAndIncrement();
            m.setId(newId);
            storage.put(newId, m);
            log.info("Meal with newId={} saved", newId);
            return m;
        } else {
            if (storage.get(oldId) != null) {
                storage.put(oldId, m);
                log.info("Meal with Id={}  updated", oldId);
                return m;
            }
            return null;
        }
    }

    public boolean delete(int id) {
        log.info("Delete id={}", id);
        Meal meal = storage.remove(id);
        if (meal != null) {
            log.info("Meal with id={} deleted", id);
        } else {
            log.info("Meal with id={} was not have mapping value", id);
        }
        return meal != null;
    }

    public Meal get(int id) {
        log.info("Get id={}", id);
        Meal meal = storage.get(id);
        if (meal != null) {
            log.info("Meal with id={} was gotten", id);
        } else {
            log.info("Meal with id={} have no mapping value", id);
        }
        return meal;
    }
}