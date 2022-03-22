package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapMealStorage extends AbstractStorage<String> {
    private final Map<String, Meal> storage = MealsUtil.createMealData();

    protected boolean isExist(String searchKey) {
        return searchKey != null;
    }

    @Override
    protected String getSearchKey(String id) {
        return (storage.containsKey(id)) ? id : null;
    }

    @Override
    protected void doSave(Meal m, String searchKey) {
        storage.put(m.getId(), m);
        System.out.println("Meal with Key= " + m.getId() + " is mapped");
    }

    @Override
    protected void doUpdate(Meal meal, String searchKey) {
        storage.replace(meal.getId(), meal);
    }

    @Override
    protected void doDelete(String key) {
        storage.remove(key);
    }

    @Override
    protected Meal doGet(String key) {
        return storage.get(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    protected List<Meal> getStorage() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.values().size();
    }
}