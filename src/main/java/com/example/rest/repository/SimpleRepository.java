package com.example.rest.repository;

import com.example.rest.exceptions.NoSuchEntityException;

import java.sql.SQLException;

public interface SimpleRepository<T, K> {
    T findById(K id) throws NoSuchEntityException, SQLException;

    boolean deleteById(K id) throws SQLException;

    boolean add (T t) throws SQLException;

    boolean update (T t) throws NoSuchEntityException, SQLException;
}
