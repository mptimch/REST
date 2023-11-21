package com.example.rest.repository.impl;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Author;
import com.example.rest.repository.AuthorRepository;
import com.example.rest.repository.mapper.AuthorResultSetMapperImpl;

import java.sql.*;

public class AuthorRepositoryImpl implements AuthorRepository {
    private AuthorResultSetMapperImpl resultSetMapper = RepositoryMapperStorage.getAuthorResultSetMapper();

    private Connection connection;

    public AuthorRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Author findById(Integer id) throws NoSuchEntityException {
        Author author = new Author();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM author WHERE id = " + id + ";")) {

            // проверяем, получили ли мы что-то из базы
            if (!resultSet.next()) {
                String message = "Пользователь с id " + id + " не найден";
                throw new NoSuchEntityException(id, message);
            }
            resultSetMapper = RepositoryMapperStorage.getAuthorResultSetMapper();
            author = resultSetMapper.map(resultSet);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return author;
    }


    @Override
    public boolean deleteById(Integer id) {
        boolean succsess = false;
        try (Statement statement = connection.createStatement()) {
            succsess = statement.execute("DELETE FROM author WHERE id = " + id + ";");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return succsess;
    }


    @Override
    public boolean add(Author author) throws IllegalArgumentException {
        boolean result = false;
        String name = author.getName();
        String sql = "INSERT INTO author (name) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
        String sql = "UPDATE author SET name = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            findById(author.getId());

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
