package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {

    Meal save(Meal m);

    Meal get(int id);

    boolean delete(int id);

    List<Meal> getAll();
}