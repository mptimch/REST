package com.example.rest.repository.mapper;

import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.BookRepository;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import com.example.rest.repository.impl.RepositoryMapperStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreResultSetMapperImpl implements GeneralResultSetMapper{

    @Override
    public Genre map(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        genre.setBooks(RepositoryMapperStorage.getBookRepository().getBooksByGenreId(resultSet.getInt("id")));
        return genre;
    }

    public Genre getGenresByBookId(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }
}
