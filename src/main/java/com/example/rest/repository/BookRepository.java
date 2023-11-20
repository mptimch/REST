package com.example.rest.repository;


import com.example.rest.model.Book;
import com.example.rest.model.Genre;

import java.util.List;

public interface BookRepository extends SimpleRepository<Book, Integer>{

    List<Book> getBooksByAuthorId(int id);

    List <Book> getBooksByGenreId(int id);
}
