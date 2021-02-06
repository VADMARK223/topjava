package ru.javawebinar.topjava.service;

import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Markitanov Vadim
 * @since 06.02.2021
 */
public class MealRepository implements TopjavaRepository<MealTo, Long> {
    private final int caloriesPerDay = 150;
    private final List<MealTo> meals = new ArrayList<>();

    public MealRepository() {
        meals.add(new MealTo(1L, LocalDateTime.now(), "Test1", 100, true));
        meals.add(new MealTo(2L, LocalDateTime.now(), "Test2", 200, true));
    }

    @Override
    public List<MealTo> findAll() {
        return meals;
    }

    @Override
    public Optional<MealTo> findById(Long id) {
        return meals.parallelStream().filter(mealTo -> mealTo.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteById(Long id) {
        delete(findById(id).orElseThrow(() -> new RuntimeException(String.format("No mealTo entity with %s exists!", id))));
    }

    private void delete(MealTo mealTo) {
        meals.removeIf(mealTo1 -> mealTo1.getId().equals(mealTo.getId()));
    }
}
