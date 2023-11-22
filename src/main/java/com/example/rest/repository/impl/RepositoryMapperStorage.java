package com.example.rest.repository.impl;

import com.example.rest.repository.mapper.AuthorResultSetMapperImpl;
import com.example.rest.repository.mapper.BookResultSetMapperImpl;
import com.example.rest.repository.mapper.GenreResultSetMapperImpl;
import com.mysql.cj.jdbc.ConnectionImpl;
import db.impl.ConnectionManagerImpl;

import java.sql.SQLException;

public class RepositoryMapperStorage {
    static AuthorRepositoryImpl authorRepository;

    static {
        try {
            authorRepository = new AuthorRepositoryImpl(new ConnectionManagerImpl().getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static BookRepositoryImpl bookRepository;

    static {
        try {
            bookRepository = new BookRepositoryImpl(new ConnectionManagerImpl().getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static GenreRepositoryImpl genreRepository;

    static {
        try {
            genreRepository = new GenreRepositoryImpl(new ConnectionManagerImpl().getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    static AuthorResultSetMapperImpl authorResultSetMapper = new AuthorResultSetMapperImpl();
//    static BookResultSetMapperImpl bookResultSetMapper = new BookResultSetMapperImpl();
//    static GenreResultSetMapperImpl genreResultSetMapper = new GenreResultSetMapperImpl();

    public static AuthorRepositoryImpl getAuthorRepository() {
        return authorRepository;
    }

    public static BookRepositoryImpl getBookRepository() {
        return bookRepository;
    }

    public static GenreRepositoryImpl getGenreRepository() {
        return genreRepository;
    }

//    public static AuthorResultSetMapperImpl getAuthorResultSetMapper() {
//        return authorResultSetMapper;
//    }
//
//    public static BookResultSetMapperImpl getBookResultSetMapper() {
//        return bookResultSetMapper;
//    }
//
//    public static GenreResultSetMapperImpl getGenreResultSetMapper() {
//        return genreResultSetMapper;
//    }
}
