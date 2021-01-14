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
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.UserMealsUtil.ConsoleColor.*;

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

        println("===========CYCLES===========", YELLOW_BOLD);
        applyAndPrintResFilteredFunc(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000, UserMealsUtil::filteredByCycles);

        println("===========STREAMS===========", YELLOW_BOLD);
        applyAndPrintResFilteredFunc(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000, UserMealsUtil::filteredByStreams);
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
            if (WrapperLocalDateTime.of(userMeal.getDateTime()).isBetween(startTime, endTime)) {
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
        final Map<LocalDate, Integer> localDateToCaloriesMap = new HashMap<>();
        meals.forEach(userMeal -> localDateToCaloriesMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum));

        return meals.parallelStream()
                .filter(userMeal -> WrapperLocalDateTime.of(userMeal.getDateTime()).isBetween(startTime, endTime))
                .map(userMeal -> {
                    int totalCalories = localDateToCaloriesMap.get(userMeal.getDateTime().toLocalDate());
                    return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), totalCalories > caloriesPerDay);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param userMealWithExcess - Instance of {@link UserMealWithExcess}
     * @return The value of the excess field
     */
    private static boolean getExcessFieldValue(UserMealWithExcess userMealWithExcess) {
        boolean result = false;
        try {
            Field excessField = UserMealWithExcess.class.getDeclaredField("excess");
            excessField.setAccessible(true);
            result = (boolean) excessField.get(userMealWithExcess);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Print an Object and then terminate a colored line.
     * @param x         The Object to be printed.
     * @param ansiColor ANSI color value the object to be printed.
     */
    private static void println(Object x, String ansiColor) {
        System.out.println(ansiColor + x + ConsoleColor.RESET);
    }

    /**
     * Inner wrapper class for methods {@linkplain #isAfter(ChronoLocalDateTime)} and {@linkplain #isBetween(LocalTime, LocalTime)}.
     * @author Markitanov Vadim
     * @since 14.01.2021
     */
    private static class WrapperLocalDateTime implements ChronoLocalDateTime<LocalDate> {
        private final LocalDateTime localDateTime;

        public static WrapperLocalDateTime of(LocalDateTime localDateTime) {
            return new WrapperLocalDateTime(localDateTime);
        }

        private WrapperLocalDateTime(LocalDateTime localDateTime) {
            Objects.requireNonNull(localDateTime, "Local date-time is null.");
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

        public boolean isBetween(LocalTime startTime, LocalTime endTime) {
            LocalDateTime startLocalDateTime = LocalDate.from(this).atTime(startTime.getHour(), startTime.getMinute());
            LocalDateTime endLocalDateTime = LocalDate.from(this).atTime(endTime.getHour(), endTime.getMinute());

            return this.isAfter(startLocalDateTime) && this.isBefore(endLocalDateTime);
        }
    }

    /**
     * Util class for ANSI colors.
     * @author Markitanov Vadim
     * @since 14.01.2021
     */
    public static class ConsoleColor {
        public static final String RESET = "\u001B[0m";

        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";

        public static final String YELLOW_BOLD = "\033[1;33m";
    }

    /**
     * @author Markitanov Vadim
     * @since 14.01.2021
     */
    @FunctionalInterface
    private interface FilterFunction<T, T1, T2, T3, R> {
        R apply(T t, T1 t1, T2 t2, T3 t3);
    }

    @SuppressWarnings("SameParameterValue")
    private static void applyAndPrintResFilteredFunc(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay, FilterFunction<List<UserMeal>, LocalTime, LocalTime, Integer, List<UserMealWithExcess>> function) {
        function.apply(meals, startTime, endTime, caloriesPerDay)
                .forEach(userMealWithExcess -> println(userMealWithExcess, getExcessFieldValue(userMealWithExcess) ? RED : GREEN));
    }
}
