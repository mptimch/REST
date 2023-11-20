package com.example.rest.repository.mapper;

import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.BookRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreResultSetMapperImpl implements GeneralResultSetMapper{
    private BookRepository bookRepository;

    @Override
    public Genre map(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        genre.setBooks(bookRepository.getBooksByGenreId(resultSet.getInt("id")));
        return genre;
    }
}
