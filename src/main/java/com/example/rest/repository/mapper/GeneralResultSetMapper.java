package com.example.rest.repository.mapper;

import com.example.rest.exceptions.NoSuchEntityException;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface GeneralResultSetMapper <T> {

    T map (ResultSet resultSet) throws SQLException, NoSuchEntityException;
}
