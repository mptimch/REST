package com.example.rest.service;

import com.example.rest.exceptions.NoSuchEntityException;

import java.sql.SQLException;

public interface SimpleService <T> {

    String findById(int id) throws SQLException, NoSuchEntityException;

    boolean delete (int id) throws SQLException, NoSuchEntityException;

    boolean add (T t) throws SQLException, IllegalArgumentException;

    boolean update (T t) throws SQLException, NoSuchEntityException;
}
