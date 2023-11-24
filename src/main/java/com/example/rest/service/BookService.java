package com.example.rest.service;

import com.example.rest.dto.BookIncomingDTO;
import com.example.rest.dto.BookToResponseDTO;
import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.google.gson.Gson;
import db.impl.ConnectionManagerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookService implements SimpleService<BookIncomingDTO> {
    BookRepositoryImpl bookRepository;
    ConnectionManagerImpl connectionManager;

    public BookService(BookRepositoryImpl bookRepository) {
        this.bookRepository = bookRepository;
        connectionManager = new ConnectionManagerImpl();
    }

    public BookService(BookRepositoryImpl bookRepository, ConnectionManagerImpl connectionManager) {
        this.bookRepository = bookRepository;
        this.connectionManager = connectionManager;
    }

    @Override
    public String findById(int id) throws SQLException, NoSuchEntityException {
        try (Connection connection = connectionManager.getConnection()) {
            Book book = bookRepository.findById(id, connection);
            BookToResponseDTO dto = new BookToResponseDTO();
            fillBookToResponseDTO(dto, book);

            Gson gson = new Gson();
            String json = gson.toJson(dto);
            return json;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException, NoSuchEntityException {
        try (Connection connection = connectionManager.getConnection()) {
            boolean isDeleted = bookRepository.deleteById(id, connection);
            return isDeleted;
        }
    }

    @Override
    public boolean add(BookIncomingDTO dto) throws SQLException, IllegalArgumentException, NumberFormatException {
        boolean result = false;
        Book book = new Book();
        book.setId(dto.getId());
        book.setName(dto.getName());
        book.setPrice(dto.getPrice());
        try (Connection connection = connectionManager.getConnection()) {
            if (dto.getGenresId() == null) {
                result = bookRepository.add(book, connection);
            } else {
                result = bookRepository.add(book, dto.getAuthorId(), dto.getGenresId(), connection);
            }
        }
        return result;
    }

    @Override
    public boolean update(BookIncomingDTO dto) throws SQLException, NoSuchEntityException, NumberFormatException {
        boolean result = false;
        Book book = new Book();
        book.setId(dto.getId());
        book.setName(dto.getName());
        book.setPrice(dto.getPrice());
        try (Connection connection = connectionManager.getConnection()) {
            result = bookRepository.update(book, connection);
        }
        return result;
    }

    void fillBookToResponseDTO(BookToResponseDTO dto, Book book) {
        dto.setName(book.getName());
        dto.setPrice(book.getPrice());
        dto.setAuthor(book.getAuthor().getName());
        List<Genre> genres = book.getGenres();
        List<String> genreNames = genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
        dto.setGenres(genreNames);
    }
}
