package com.example.rest.repository.impl;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Author;
import com.example.rest.repository.AuthorRepository;
import com.example.rest.repository.mapper.AuthorResultSetMapperImpl;
import db.impl.ConnectionManagerImpl;

import java.sql.*;

public class AuthorRepositoryImpl implements AuthorRepository {
    private AuthorResultSetMapperImpl resultSetMapper = RepositoryMapperStorage.getAuthorResultSetMapper();
    private ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    @Override
    public Author findById(Integer id) throws NoSuchEntityException {
        Author author = new Author();
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM author WHERE id = " + id + ";");

            // проверяем, получили ли мы что-то из базы
            if (!resultSet.next()) {
                String message = "Пользователь с id " + id + " не найден";
                throw new NoSuchEntityException(id, message);
            }
            resultSetMapper = RepositoryMapperStorage.getAuthorResultSetMapper();
            author = resultSetMapper.map(resultSet);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return author;
    }


    @Override
    public boolean deleteById(Integer id) {
        boolean succsess = false;
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            succsess = statement.execute("DELETE FROM author WHERE id = " + id + ";");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return succsess;
    }


    @Override
    public boolean add(Author author) throws IllegalArgumentException {
        boolean result = false;
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            String name = author.getName();
            String sql = "INSERT INTO author (name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            int count = preparedStatement.executeUpdate();

            if (count != 1) {
                throw new IllegalArgumentException();
            }
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return result;
    }


    @Override
    public boolean update(Author author) throws NoSuchEntityException {
        boolean success = false;
        try {
            findById(author.getId());
            connection = connectionManager.getConnection();
            String sql = "UPDATE author SET name = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, author.getName());
            statement.setInt(2, author.getId());
            int rowsUpdated = statement.executeUpdate();
            success = rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

}
