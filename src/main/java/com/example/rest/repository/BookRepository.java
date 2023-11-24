package com.example.rest.repository;

import com.example.rest.model.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BookRepository extends SimpleRepository<Book, Integer>{

    List<Book> getBooksByAuthorId(int id) throws SQLException;

    List <Book> getBooksByGenreId(int id) throws SQLException;
}
