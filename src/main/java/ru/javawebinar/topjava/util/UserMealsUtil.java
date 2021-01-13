package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.lang.reflect.Field;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.*;

public class UserMealsUtil {
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

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println("===========CYCLES===========");
        mealsTo.forEach(userMealWithExcess -> {
            boolean excessValue = false;
            try {
                Field excessField = UserMealWithExcess.class.getDeclaredField("excess");
                excessField.setAccessible(true);
                excessValue = (boolean) excessField.get(userMealWithExcess);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (excessValue) {
                System.err.println(userMealWithExcess);
            } else {
                System.out.println(userMealWithExcess);
            }
        });

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("Start time is after end time.");
        }
        final Map<LocalDate, Integer> localDateToCaloriesMap = new HashMap<>();
        for (UserMeal userMeal : meals) {
            localDateToCaloriesMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }

        final List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            WrapperLocalDateTime userMealDateTime = new WrapperLocalDateTime(userMeal.getDateTime());
            LocalDateTime startLocalDateTime = LocalDate.from(userMealDateTime).atTime(startTime.getHour(), startTime.getMinute());
            LocalDateTime endLocalDateTime = LocalDate.from(userMealDateTime).atTime(endTime.getHour(), endTime.getMinute());

            if (userMealDateTime.isAfter(startLocalDateTime) && userMealDateTime.isBefore(endLocalDateTime)) {
                int totalCalories = localDateToCaloriesMap.get(userMeal.getDateTime().toLocalDate());
                result.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), totalCalories > caloriesPerDay));
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams

        return null;
    }

    /**
     * Author: Markitanov Vadim
     * Date: 14.01.2021
     * Inner wrapper class for method {@link #isAfter(ChronoLocalDateTime)} overriding.
     */
    private static class WrapperLocalDateTime implements ChronoLocalDateTime<LocalDate> {
        private final LocalDateTime localDateTime;

        private WrapperLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }

        @Override
        public LocalDate toLocalDate() {
            return localDateTime.toLocalDate();
        }

        @Override
        public LocalTime toLocalTime() {
            return localDateTime.toLocalTime();
        }

        @Override
        public boolean isSupported(TemporalField field) {
            return localDateTime.isSupported(field);
        }

        @Override
        public long getLong(TemporalField field) {
            return localDateTime.getLong(field);
        }

        @Override
        public ChronoLocalDateTime<LocalDate> with(TemporalField field, long newValue) {
            return localDateTime.with(field, newValue);
        }

        @Override
        public ChronoLocalDateTime<LocalDate> plus(long amountToAdd, TemporalUnit unit) {
            return localDateTime.plus(amountToAdd, unit);
        }

        @Override
        public long until(Temporal endExclusive, TemporalUnit unit) {
            return localDateTime.until(endExclusive, unit);
        }

        @Override
        public ChronoZonedDateTime<LocalDate> atZone(ZoneId zone) {
            return localDateTime.atZone(zone);
        }

        @Override
        public boolean isAfter(ChronoLocalDateTime<?> other) {
            long thisEpDay = this.toLocalDate().toEpochDay();
            long otherEpDay = other.toLocalDate().toEpochDay();

            return thisEpDay > otherEpDay ||
                    (thisEpDay == otherEpDay && this.toLocalTime().toNanoOfDay() >= other.toLocalTime().toNanoOfDay());
        }
    }
}
