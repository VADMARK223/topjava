package ru.javawebinar.topjava.service;

import java.util.List;
import java.util.Optional;

/**
 * @author Markitanov Vadim
 * @since 06.02.2021
 */
public interface TopjavaRepository<E, ID> {
    List<E> findAll();

    Optional<E> findById(ID id);

    void deleteById(ID id);

    <S extends E> S save(S entity);
}
