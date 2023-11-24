package com.example.rest.repository;

import com.example.rest.model.Genre;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GenreRepository extends SimpleRepository<Genre, Integer> {

    List<Genre> getGenresByBookId(int id, Connection connection) throws SQLException;
}
