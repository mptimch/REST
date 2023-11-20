package com.example.rest.repository;

import com.example.rest.model.Book;
import com.example.rest.model.Genre;

import java.util.List;

public interface GenreRepository extends  SimpleRepository <Genre, Integer>{

    List<Genre> getGenresByBookId(int id);
}
