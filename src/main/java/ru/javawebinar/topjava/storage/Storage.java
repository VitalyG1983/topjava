package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {

    void clear();

    void save(Meal r);

    Meal get(Integer uuid);

    void delete(Integer uuid);

    List<Meal> getAllSorted();

    int size();

    void update(Meal resume);
}