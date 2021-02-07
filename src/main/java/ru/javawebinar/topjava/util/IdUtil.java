package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Markitanov Vadim
 * @since 08.02.2021
 */
public class IdUtil {
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    public static long getId() {
        return counter.getAndIncrement();
    }
}
