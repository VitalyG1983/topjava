package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Meal {
    private final String id;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;

    public Meal() {
        this.id = UUID.randomUUID().toString();
        this.dateTime = LocalDateTime.now();
        this.description = "meal";
        this.calories = 0;
    }

    public Meal(String id) {
        this.id = id;
        this.dateTime = LocalDateTime.now();
        this.description = "meal";
        this.calories = 0;
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.id = UUID.randomUUID().toString();
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Meal(String id, LocalDateTime dateTime, String description, int calories) {
        this.id = UUID.randomUUID().toString();
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getId() {
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