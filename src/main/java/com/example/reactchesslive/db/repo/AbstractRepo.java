package com.example.reactchesslive.db.repo;

import com.example.reactchesslive.model.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface AbstractRepo<T extends AbstractEntity> {

    void save(T entity);

    void delete(T entity);

    T merge(T entity);

    void deleteById(Object id);

    Optional<T> findById(Object id);

    List<T> findAll();
}