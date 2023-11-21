package com.example.rest.repository.mapper;

import com.example.rest.model.Author;
import com.example.rest.repository.BookRepository;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.example.rest.repository.impl.RepositoryMapperStorage;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthorResultSetMapperImpl implements GeneralResultSetMapper {


    @Override
    public Author map(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        int id = resultSet.getInt("id");
        author.setId(id);
        author.setName(resultSet.getString("name"));
        author.setBooks(RepositoryMapperStorage.getBookRepository().getBooksByAuthorId(id));
        return author;
    }
}
