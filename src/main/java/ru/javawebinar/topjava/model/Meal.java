package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Meal {
    private final Integer id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private static AtomicInteger idCounter = new AtomicInteger(0);

    public Meal() {
        this.id = idCounter.getAndIncrement();
        this.dateTime = LocalDateTime.now();
        this.description = "meal";
        this.calories = 0;

    }

    public Meal(int id) {
        this.id = id;
        this.dateTime = LocalDateTime.now();
        this.description = "meal";
        this.calories = 0;
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.id = idCounter.getAndIncrement();
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Meal(int id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}