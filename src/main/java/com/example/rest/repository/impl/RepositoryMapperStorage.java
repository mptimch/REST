package com.example.rest.repository.impl;

import com.example.rest.repository.mapper.AuthorResultSetMapperImpl;
import com.example.rest.repository.mapper.BookResultSetMapperImpl;
import com.example.rest.repository.mapper.GenreResultSetMapperImpl;

public class RepositoryMapperStorage {
    static AuthorRepositoryImpl authorRepository = new AuthorRepositoryImpl();
    static BookRepositoryImpl bookRepository = new BookRepositoryImpl();
    static GenreRepositoryImpl genreRepository = new GenreRepositoryImpl();

    static AuthorResultSetMapperImpl authorResultSetMapper = new AuthorResultSetMapperImpl();
    static BookResultSetMapperImpl bookResultSetMapper = new BookResultSetMapperImpl();
    static GenreResultSetMapperImpl genreResultSetMapper = new GenreResultSetMapperImpl();

    public static AuthorRepositoryImpl getAuthorRepository() {
        return authorRepository;
    }

    public static BookRepositoryImpl getBookRepository() {
        return bookRepository;
    }

    public static GenreRepositoryImpl getGenreRepository() {
        return genreRepository;
    }

    public static AuthorResultSetMapperImpl getAuthorResultSetMapper() {
        return authorResultSetMapper;
    }

    public static BookResultSetMapperImpl getBookResultSetMapper() {
        return bookResultSetMapper;
    }

    public static GenreResultSetMapperImpl getGenreResultSetMapper() {
        return genreResultSetMapper;
    }
}
