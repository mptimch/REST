package com.example.rest.repository.mapper;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.repository.AuthorRepository;
import com.example.rest.repository.BookRepository;
import com.example.rest.repository.GenreRepository;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import com.example.rest.repository.impl.RepositoryMapperStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookResultSetMapperImpl implements GeneralResultSetMapper  {

    @Override
    public Book map(ResultSet resultSet) throws SQLException, NoSuchEntityException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setName(resultSet.getString("name"));
        book.setPrice(resultSet.getInt("price"));
        int authorId = resultSet.getInt("author_id");

        book.setAuthor(RepositoryMapperStorage.getAuthorRepository().findById(authorId));
        book.setGenres(RepositoryMapperStorage.getGenreRepository().getGenresByBookId(resultSet.getInt("id")));
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
