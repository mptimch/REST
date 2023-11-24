package com.example.rest.repository;

import com.example.rest.exceptions.NoSuchEntityException;

import java.sql.Connection;
import java.sql.SQLException;

public interface SimpleRepository<T, K> {
    T findById(K id, Connection connection) throws NoSuchEntityException, SQLException;

    boolean deleteById(K id, Connection connection) throws SQLException;

    boolean add (T t, Connection connection) throws SQLException;

    boolean update (T t, Connection connection) throws NoSuchEntityException, SQLException;
}
