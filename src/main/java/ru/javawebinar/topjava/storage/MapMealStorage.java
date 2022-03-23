package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapMealStorage extends AbstractStorage<Integer> {
    private final Map<Integer, Meal> storage = MealsUtil.createMealData();

    protected boolean isExist(Integer searchKey) {
        return searchKey != null;
    }

    @Override
    protected Integer getSearchKey(Integer id) {
        return (storage.containsKey(id)) ? id : null;
    }

    @Override
    protected void doSave(Meal m, Integer searchKey) {
        storage.put(m.getId(), m);
        System.out.println("Meal with Key= " + m.getId() + " is mapped");
    }

    @Override
    protected void doUpdate(Meal meal, Integer searchKey) {
        storage.replace(meal.getId(), meal);
    }

    @Override
    protected void doDelete(Integer key) {
        storage.remove(key);
    }

    @Override
    protected Meal doGet(Integer key) {
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