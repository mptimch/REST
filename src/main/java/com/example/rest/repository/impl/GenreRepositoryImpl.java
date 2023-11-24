package com.example.rest.repository.impl;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Genre;
import com.example.rest.repository.GenreRepository;
import com.example.rest.repository.mapper.GenreResultSetMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreRepositoryImpl implements GenreRepository {


    @Override
    public Genre findById(Integer id, Connection connection) throws NoSuchEntityException, SQLException {
        Genre genre = new Genre();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM genre WHERE id = " + id + ";")) {

            if (!resultSet.next()) {
                String message = "Жанр с id " + id + " не найден";
                throw new NoSuchEntityException(id, message);
            }
            genre = new GenreResultSetMapperImpl(connection).map(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genre;
    }

    @Override
    public boolean deleteById(Integer id, Connection connection) throws SQLException {
        boolean succsess = false;

        try (Statement statement = connection.createStatement()) {
            succsess = statement.execute("DELETE FROM genre WHERE id = " + id + ";");
            return succsess;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return succsess;
    }

    @Override
    public boolean add(Genre genre, Connection connection) throws IllegalArgumentException, SQLException {
        boolean result = false;
        String name = genre.getName();
        String sql = "INSERT INTO genre (name) VALUES (?)";

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


    public boolean add(Genre genre, List<Integer> booksId, Connection connection) throws IllegalArgumentException, SQLException {
        boolean result = false;
        int genreId = 0;
        result = add(genre, connection);
        String query = "SELECT MAX(id) FROM genre;";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
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
            insertBookGenreStatement.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(Genre genre, Connection connection) throws NoSuchEntityException, SQLException {
        boolean success = false;
        String sql = "UPDATE genre SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, genre.getName());
            statement.setInt(2, genre.getId());
            int rowsUpdated = statement.executeUpdate();
            success = rowsUpdated > 0;

            if (!success) {
                String message = "Жанров с id " + genre.getId() + " не найдено";
                throw new NoSuchEntityException(genre.getId(), message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }


    public boolean update(Genre genre, List<Integer> booksId, Connection connection) throws NoSuchEntityException, SQLException {
        update(genre, connection);
        boolean success = false;
        String deleteBookGenreQuery = "DELETE FROM book_genre WHERE genre_id = ?";
        String insertBookGenreQuery = "INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)";

        try (PreparedStatement deleteBookGenreStatement = connection.prepareStatement(deleteBookGenreQuery);
             PreparedStatement insertBookGenreStatement = connection.prepareStatement(insertBookGenreQuery)) {

            deleteBookGenreStatement.setInt(1, genre.getId());
            deleteBookGenreStatement.executeUpdate();

            for (int bookId : booksId) {
                insertBookGenreStatement.setInt(1, bookId);
                insertBookGenreStatement.setInt(2, genre.getId());
                insertBookGenreStatement.addBatch();
            }
            insertBookGenreStatement.executeBatch();
            insertBookGenreStatement.close();

            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public List<Genre> getGenresByBookId(int id, Connection connection) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.id, g.name" +
                "FROM genre g" +
                "JOIN book_genre bg ON g.id = bg.genre_id " +
                "WHERE bg.book_id = " + id + ";";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (!resultSet.next()) {
                String message = "Книг с id " + id + " не найден";
                throw new NoSuchEntityException(id, message);
            }

            while (resultSet.next()) {
                Genre genre = new GenreResultSetMapperImpl(connection).getGenresByBookId(resultSet);
                genres.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genres;
    }
}
