package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Markitanov Vadim
 * @since 13.02.2021
 */
public class UsersUtil {
    public static Map<Integer, User> users = new ConcurrentHashMap<>(
            Stream.of(
                    new AbstractMap.SimpleEntry<>(123, new User(1, "Admin", "admin@mail.ru", "123", Role.ADMIN)),
                    new AbstractMap.SimpleEntry<>(123, new User(2, "User", "user@mail.ru", "123", Role.USER))
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
    );
}
