package com.example.rest.service;

import com.example.rest.dto.AuthorIncomingDTO;
import com.example.rest.dto.AuthorToResponseDTO;
import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Author;
import com.example.rest.model.Book;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorService implements SimpleService<AuthorIncomingDTO> {
    AuthorRepositoryImpl authorRepository;

    public AuthorService(AuthorRepositoryImpl authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public String findById(int id) throws SQLException, NoSuchEntityException {
        Author author = authorRepository.findById(id);
        AuthorToResponseDTO dto = new AuthorToResponseDTO();
        dto.setName(author.getName());
        List<Book> books = author.getBooks();
        List<String> booksNames = books.stream()
                .map(Book::getName)
                .collect(Collectors.toList());
        dto.setBooks(booksNames);

        Gson gson = new Gson();
        String json = gson.toJson(dto);
        return json;
    }

    @Override
    public boolean delete(int id) throws SQLException, NoSuchEntityException {
        boolean isDeleted = authorRepository.deleteById(id);
        return isDeleted;
    }

    @Override
    public boolean add(AuthorIncomingDTO dto) throws SQLException, IllegalArgumentException {
        boolean result = false;
        Author author = new Author();
        author.setId(dto.getId());
        author.setName(dto.getName());
        result = authorRepository.add(author);
        return result;
    }

    @Override
    public boolean update(AuthorIncomingDTO dto) throws SQLException, NoSuchEntityException {
        boolean result = false;
        Author author = new Author();
        author.setId(dto.getId());
        author.setName(dto.getName());
        result = authorRepository.update(author);
        return result;
    }
}
