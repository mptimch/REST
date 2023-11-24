package com.example.rest.repository.mapper;

import com.example.rest.model.Author;
import com.example.rest.repository.impl.BookRepositoryImpl;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorResultSetMapperImpl implements GeneralResultSetMapper {

    Connection connection;

    public AuthorResultSetMapperImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Author map(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        int id = resultSet.getInt("id");
        author.setId(id);
        author.setName(resultSet.getString("name"));
        author.setBooks(new BookRepositoryImpl().getBooksByAuthorId(id, connection));
        return author;
    }
}
