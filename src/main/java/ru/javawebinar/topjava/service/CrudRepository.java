package ru.javawebinar.topjava.service;

import java.util.List;
import java.util.Optional;

/**
 * @author Markitanov Vadim
 * @since 06.02.2021
 */
public interface CrudRepository<E, ID> {
    List<E> findAll();

    Optional<E> findById(ID id);

    boolean deleteById(ID id);

    boolean create(E entity);
}
