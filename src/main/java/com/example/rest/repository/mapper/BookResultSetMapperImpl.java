package com.example.rest.repository.mapper;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
import com.example.rest.repository.impl.GenreRepositoryImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookResultSetMapperImpl implements GeneralResultSetMapper {

    Connection connection;
    public BookResultSetMapperImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Book map(ResultSet resultSet) throws SQLException, NoSuchEntityException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setName(resultSet.getString("name"));
        book.setPrice(resultSet.getInt("price"));
        int authorId = resultSet.getInt("author_id");

        book.setAuthor(new AuthorRepositoryImpl().findById(authorId, connection));
        book.setGenres(new GenreRepositoryImpl().getGenresByBookId(resultSet.getInt("id"), connection));
        return book;
    }

    public Book getBooksByAuthorId(ResultSet resultSet) throws SQLException, NoSuchEntityException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setName(resultSet.getString("name"));
        int authorId = resultSet.getInt("author_id");
        return book;
    }
}
