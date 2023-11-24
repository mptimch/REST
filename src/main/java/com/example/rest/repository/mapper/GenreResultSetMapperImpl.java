package com.example.rest.repository.mapper;

import com.example.rest.model.Genre;
import com.example.rest.repository.impl.BookRepositoryImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreResultSetMapperImpl implements GeneralResultSetMapper {

    Connection connection;

    public GenreResultSetMapperImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Genre map(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        genre.setBooks(new BookRepositoryImpl().getBooksByGenreId(resultSet.getInt("id"), connection));
        return genre;
    }

    public Genre getGenresByBookId(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }
}
