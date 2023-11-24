package com.example.rest.repository.mapper;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import db.impl.ConnectionManagerImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookResultSetMapperImpl implements GeneralResultSetMapper {

    ConnectionManagerImpl connectionManager;
    public BookResultSetMapperImpl(ConnectionManagerImpl connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Book map(ResultSet resultSet) throws SQLException, NoSuchEntityException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setName(resultSet.getString("name"));
        book.setPrice(resultSet.getInt("price"));
        int authorId = resultSet.getInt("author_id");

        book.setAuthor(new AuthorRepositoryImpl(connectionManager).findById(authorId));
        book.setGenres(new GenreRepositoryImpl(connectionManager).getGenresByBookId(resultSet.getInt("id")));
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
