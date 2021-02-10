package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Markitanov Vadim
 * @since 07.02.2021
 */
public class MealInMemoryCrudRepository implements CrudRepository<Meal, Long> {
    private final AtomicLong counter = new AtomicLong(0);
    private final List<Meal> meals = new CopyOnWriteArrayList<>();


    public MealInMemoryCrudRepository() {
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500).setId(generateID()));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000).setId(generateID()));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500).setId(generateID()));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100).setId(generateID()));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000).setId(generateID()));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500).setId(generateID()));
        create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410).setId(generateID()));
    }

    @Override
    public List<Meal> findAll() {
        return meals;
    }

    @Override
    public Optional<Meal> findById(Long id) {
        return meals.parallelStream().filter(meal -> meal.getId().equals(id)).findFirst();
    }

    @Override
    public boolean deleteById(Long id) {
        return meals.removeIf(mealItem -> mealItem.getId().equals(id));
    }

    @Override
    public Meal create(Meal entity) {
        entity.setId(counter.getAndIncrement());
        meals.add(entity);
        return entity;
    }

    @Override
    public Meal update(Meal newMeal) {
        Optional<Meal> mealOptional = this.findById(newMeal.getId());
        if (mealOptional.isPresent()) {
            Meal meal = mealOptional.get();
            meal.setDateTime(newMeal.getDateTime());
            meal.setDescription(newMeal.getDescription());
            meal.setCalories(newMeal.getCalories());
            return meal;
        }

        return null;
    }

    private Long generateID() {
        return counter.getAndIncrement();
    }
}
