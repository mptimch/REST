package com.example.rest.repository;

import com.example.rest.exceptions.NoSuchEntityException;

public interface SimpleRepository<T, K> {
    T findById(K id) throws NoSuchEntityException;

    boolean deleteById(K id);

    boolean add (T t);

    boolean update (T t) throws NoSuchEntityException;
}
