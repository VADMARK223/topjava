package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import static ru.javawebinar.topjava.util.MealsUtil.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Markitanov Vadim
 * @since 07.02.2021
 */
public class MealRepository implements TopjavaRepository<Meal, Long> {
    @Override
    public List<Meal> findAll() {
        return meals;
    }

    @Override
    public Optional<Meal> findById(Long id) {
        return meals.parallelStream().filter(meal -> meal.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteById(Long id) {
        delete(findById(id).orElseThrow(() -> new RuntimeException(String.format("No meal entity with %s exists!", id))));
    }

    @Override
    public <S extends Meal> S save(S entity) {
        if (meals.contains(entity)) {
            System.out.println("CONTAIN");
            Meal meal = meals.get(meals.indexOf(entity));
        } else {
            meals.add(entity);
            System.out.println("AAAAAAAAAAAAAADDDDD: " + meals.size());
        }
        return null;
    }

    private void delete(Meal meal) {
        meals.removeIf(mealItem -> mealItem.getId().equals(meal.getId()));
    }
}
