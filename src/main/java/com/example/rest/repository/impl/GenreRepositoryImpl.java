package com.example.rest.repository.impl;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.BookRepository;
import com.example.rest.repository.GenreRepository;
import com.example.rest.repository.mapper.BookResultSetMapperImpl;
import com.example.rest.repository.mapper.GenreResultSetMapperImpl;
import db.impl.ConnectionManagerImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreRepositoryImpl implements GenreRepository {

    private GenreResultSetMapperImpl resultSetMapper = new GenreResultSetMapperImpl();
    private ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();
    private BookRepository bookRepository;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet = null;


    @Override
    public Genre findById(Integer id) throws NoSuchEntityException {
        Genre genre = new Genre();
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM 'genre' WHERE 'id' = " + id + ";");

            // проверяем, получили ли мы то-то из базы
            if (!resultSet.next()) {
                String message = "Жанр с id " + id + " не найден";
                throw new NoSuchEntityException(id, message);
            }

            genre = resultSetMapper.map(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return genre;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean succsess = false;
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            succsess = statement.execute("DELETE FROM 'genre' WHERE 'id' = " + id + ";");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return succsess;
    }

    @Override
    public boolean add(Genre genre) throws IllegalArgumentException {
        boolean result = false;
        try {
            connection = connectionManager.getConnection();
            String name = genre.getName();
            String sql = "INSERT INTO 'genre' (name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            int count = preparedStatement.executeUpdate();

            if (count != 1) {
                throw new IllegalArgumentException();
            }
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return result;
    }


    public boolean add(Genre genre, List<Integer> booksId) throws IllegalArgumentException {
        boolean result = false;
        int genreId = 0;
        try {
            connection = connectionManager.getConnection();
            result = add(genre);

            String query = "SELECT MAX(id) FROM genre;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                genreId = resultSet.getInt(1);
            }

            String insertBookGenreQuery = "INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)";
            PreparedStatement insertBookGenreStatement = connection.prepareStatement(insertBookGenreQuery);

            for (int bookId : booksId) {
                insertBookGenreStatement.setInt(1, bookId);
                insertBookGenreStatement.setInt(2, genreId);
                insertBookGenreStatement.addBatch();
            }

            insertBookGenreStatement.executeBatch();
            result = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return result;
    }


    @Override
    public boolean update(Genre genre) throws NoSuchEntityException {
        boolean success = false;
        try {
            connection = connectionManager.getConnection();
            String sql = "UPDATE genre SET name = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, genre.getName());
            statement.setInt(2, genre.getId());
            int rowsUpdated = statement.executeUpdate();
            success = rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return success;
    }


    public boolean update(Genre genre, List<Integer> booksId) throws NoSuchEntityException {
        update(genre);
        boolean success = false;

        String deleteBookGenreQuery = "DELETE FROM book_genre WHERE genre_id = ?";
        String insertBookGenreQuery = "INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)";

        try (Connection resourceConnection = connectionManager.getConnection();
             PreparedStatement deleteBookGenreStatement = resourceConnection.prepareStatement(deleteBookGenreQuery);
             PreparedStatement insertBookGenreStatement = resourceConnection.prepareStatement(insertBookGenreQuery)) {


            deleteBookGenreStatement.setInt(1, genre.getId());
            deleteBookGenreStatement.executeUpdate();

            for (int bookId : booksId) {
                insertBookGenreStatement.setInt(1, bookId);
                insertBookGenreStatement.setInt(2, genre.getId());
                insertBookGenreStatement.addBatch();
            }
            insertBookGenreStatement.executeBatch();

            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }


    @Override
    public List<Genre> getGenresByBookId(int id) {
        List<Genre> genres = new ArrayList<>();
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT g.id, g.name" +
                    "FROM genre g" +
                    "JOIN book_genre bg ON g.id = bg.genre_id " +
                    "WHERE bg.book_id = " + id + ";";
            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                String message = "Книг с id " + id + " не найден";
                throw new NoSuchEntityException(id, message);
            }


            while (resultSet.next()) {
                Genre genre = new Genre();
                genre.setId(resultSet.getInt("id"));
                genre.setName(resultSet.getString("name"));
                genres.add(genre);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return genres;
    }
}
