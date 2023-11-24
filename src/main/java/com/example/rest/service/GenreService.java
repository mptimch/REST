package com.example.rest.service;

import com.example.rest.dto.GenreIncomingDTO;
import com.example.rest.dto.GenreToResponseDTO;
import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import com.google.gson.Gson;
import db.impl.ConnectionManagerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class GenreService implements SimpleService <GenreIncomingDTO>{

    GenreRepositoryImpl genreRepository;
    ConnectionManagerImpl connectionManager;

    public GenreService(GenreRepositoryImpl genreRepository) {
        this.genreRepository = genreRepository;
         connectionManager = new ConnectionManagerImpl();
    }

    public GenreService(GenreRepositoryImpl genreRepository, ConnectionManagerImpl connectionManager) {
        this.genreRepository = genreRepository;
        this.connectionManager = connectionManager;
    }

    @Override
    public String findById(int id) throws SQLException, NoSuchEntityException {
        try (Connection connection = connectionManager.getConnection()) {
            Genre genre = genreRepository.findById(id, connection);
            GenreToResponseDTO dto = new GenreToResponseDTO();
            dto.setName(genre.getName());

            List<Book> books = genre.getBooks();
            List<String> bookNames = books.stream()
                    .map(Book::getName)
                    .collect(Collectors.toList());
            dto.setBooks(bookNames);

            Gson gson = new Gson();
            String json = gson.toJson(dto);
            return json;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException, NoSuchEntityException {
        try (Connection connection = connectionManager.getConnection()) {
            boolean isDeleted = genreRepository.deleteById(id, connection);
            return isDeleted;
        }
    }

    @Override
    public boolean add(GenreIncomingDTO dto) throws SQLException, IllegalArgumentException {
        boolean result = false;
        Genre genre = new Genre();
        genre.setId(dto.getId());
        genre.setName(dto.getName());
        try (Connection connection = connectionManager.getConnection()) {
            if (dto.getBooksId() == null) {
                result = genreRepository.add(genre, connection);
            } else {
                result = genreRepository.add(genre, dto.getBooksId(), connection);
            }
            return result;
        }
    }

    @Override
    public boolean update(GenreIncomingDTO dto) throws SQLException, NoSuchEntityException {
        boolean result = false;
        Genre genre = new Genre();
        genre.setId(dto.getId());
        genre.setName(dto.getName());
        try (Connection connection = connectionManager.getConnection()) {
            if (dto.getBooksId() == null) {
                result = genreRepository.update(genre, connection);
            } else {
                result = genreRepository.update(genre, dto.getBooksId(), connection);
            }
            return result;
        }
    }
}
