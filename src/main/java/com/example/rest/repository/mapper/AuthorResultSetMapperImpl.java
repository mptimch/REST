package com.example.rest.repository.mapper;

import com.example.rest.model.Author;
import com.example.rest.repository.BookRepository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthorResultSetMapperImpl implements GeneralResultSetMapper {
    private BookRepository bookRepository;

    @Override
    public Author map(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        int id = resultSet.getInt("id");
        author.setId(id);
        author.setName(resultSet.getString("name"));
        author.setBooks(bookRepository.getBooksByAuthorId(id));
        return author;
    }
}
