package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public synchronized Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (findByUserIdAndId(meal.getUserId(), meal.getId()).isPresent()) {
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int userId, int id) {
        if (findByUserIdAndId(userId, id).isPresent()) {
            return repository.remove(id) != null;
        }

        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        return findByUserIdAndId(userId, id).orElse(null);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream().filter(meal -> userId == meal.getUserId()).collect(Collectors.toList());
    }

    private Optional<Meal> findByUserIdAndId(int userId, int id) {
        return repository.values().stream().filter(meal -> (userId == meal.getUserId()) && (id == meal.getId())).findAny();
    }
}

