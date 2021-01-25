package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    final static Logger log = LoggerFactory.getLogger(UserMealsUtil.class);

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        log.info("===========CYCLES===========");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(userMealWithExcess -> {
            if (userMealWithExcess.isExcess()) {
                log.info(String.valueOf(userMealWithExcess));
            } else {
                log.warn(String.valueOf(userMealWithExcess));
            }
        });

        log.info("===========STREAMS===========");
        mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(userMealWithExcess -> {
            if (userMealWithExcess.isExcess()) {
                log.info(String.valueOf(userMealWithExcess));
            } else {
                log.warn(String.valueOf(userMealWithExcess));
            }
        });
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("Start time is after end time.");
        }
        final Map<LocalDate, Integer> localDateToCaloriesMap = new HashMap<>();
        meals.forEach(userMeal -> localDateToCaloriesMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum));

        final List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                int totalCalories = localDateToCaloriesMap.get(userMeal.getDateTime().toLocalDate());
                result.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), totalCalories > caloriesPerDay));
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("Start time is after end time.");
        }
        final Map<LocalDate, Integer> localDateToCaloriesMap = meals.stream()
                .collect(Collectors.toMap(userMeal -> userMeal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.parallelStream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    int totalCalories = localDateToCaloriesMap.get(userMeal.getDateTime().toLocalDate());
                    return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), totalCalories > caloriesPerDay);
                })
                .collect(Collectors.toList());
    }
}
