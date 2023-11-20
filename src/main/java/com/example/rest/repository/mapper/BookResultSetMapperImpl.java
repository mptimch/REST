package com.example.rest.repository.mapper;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.repository.AuthorRepository;
import com.example.rest.repository.BookRepository;
import com.example.rest.repository.GenreRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookResultSetMapperImpl implements GeneralResultSetMapper{
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private GenreRepository genreRepository;

    @Override
    public Book map(ResultSet resultSet) throws SQLException, NoSuchEntityException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setName(resultSet.getString("name"));
        int authorId = resultSet.getInt("author_id");
        book.setAuthor(authorRepository.findById(authorId));
        book.setGenres(genreRepository.getGenresByBookId(resultSet.getInt("id")));
        return book;
    }
}
