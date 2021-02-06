package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MealTo {
    private final Long id;
    public Long getId() {
        return id;
    }

    private final LocalDateTime dateTime;

    private final String description;
    public String getDescription() {
        return description;
    }

    private final int calories;
    public int getCalories() {
        return calories;
    }

    private final boolean excess;
    public boolean isExcess() {
        return excess;
    }

    public MealTo(Long id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public String getFormattedDateTime() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(this.dateTime);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
