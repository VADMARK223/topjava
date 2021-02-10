package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Markitanov Vadim
 * @since 07.02.2021
 */
public class MealCrudRepository implements CrudRepository<Meal, Long> {
    private static final AtomicLong counter = new AtomicLong(0);
    private static final List<Meal> meals = initMeals();

    private static List<Meal> initMeals() {
        return new CopyOnWriteArrayList<>(Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500).setId(counter.getAndIncrement()),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000).setId(counter.getAndIncrement()),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500).setId(counter.getAndIncrement()),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100).setId(counter.getAndIncrement()),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000).setId(counter.getAndIncrement()),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500).setId(counter.getAndIncrement()),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410).setId(counter.getAndIncrement())
        ));
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
        return  delete(findById(id).orElse(null));
    }

    @Override
    public boolean create(Meal entity) {
        entity.setId(counter.getAndIncrement());
        if (findById(entity.getId()).isPresent()) {
            return false;
        }

        meals.add(entity);
        return true;
    }

    private boolean delete(Meal meal) {
        if (meal == null) {
            return false;
        }

        return meals.removeIf(mealItem -> mealItem.getId().equals(meal.getId()));
    }
}
