package com.example.rest.repository.impl;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Author;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.BookRepository;
import com.example.rest.repository.GenreRepository;
import com.example.rest.repository.mapper.BookResultSetMapperImpl;
import db.impl.ConnectionManagerImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    private BookResultSetMapperImpl resultSetMapper = new BookResultSetMapperImpl();
    private ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    @Override
    public Book findById(Integer id) throws NoSuchEntityException {
        Book book = new Book();
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM 'book' WHERE 'id' = " + id + ";");

            // проверяем, получили ли мы то-то из базы
            if (!resultSet.next()) {
                String message = "Книга с id " + id + " не найдена";
                throw new NoSuchEntityException(id, message);
            }

            book = resultSetMapper.map(resultSet);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return book;
    }


    @Override
    public boolean deleteById(Integer id) {
        boolean succsess = false;
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            succsess = statement.execute("DELETE FROM 'book' WHERE 'id' = " + id + ";");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return succsess;
    }

    @Override
    public boolean add(Book book) throws IllegalArgumentException {
        boolean result = false;
        try {
            connection = connectionManager.getConnection();
            String sql = "INSERT INTO 'book' (name, price) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setInt(2, book.getPrice());
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


    public boolean add(Book book, int authorId, List<Integer> genresId) throws IllegalArgumentException {
        boolean result = false;
        try {
            int bookId = 0;
            connection = connectionManager.getConnection();
            String name = book.getName();
            String sqlToBook = "INSERT INTO 'book' (name, price, author_id) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlToBook, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, book.getPrice());
            preparedStatement.setInt(3, authorId);
            int count = preparedStatement.executeUpdate();

            if (count != 1) {
                throw new IllegalArgumentException();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                bookId = generatedKeys.getInt(1);
            }

            String insertBookGenreQuery = "INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)";
            PreparedStatement insertBookGenreStatement = connection.prepareStatement(insertBookGenreQuery);

            for (int genreId : genresId) {
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
    public boolean update(Book book) throws NoSuchEntityException {
        boolean success = false;
        try {
            // Проверяем, есть ли книга с таким id. Если е нет - ловим исключение
            findById(book.getId());

            connection = connectionManager.getConnection();
            String sql = "UPDATE book SET name = ?, price = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, book.getName());
            statement.setInt(2, book.getPrice());
            statement.setInt(3, book.getId());
            int rowsUpdated = statement.executeUpdate();
            success = rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return success;
    }


    public boolean update(Book book, int authorId, List<Integer> genresId) throws NoSuchEntityException {
        update(book);
        boolean success = false;
        String updateBookQuery = "UPDATE book SET author_id = ? WHERE id = ?";
        String deleteBookGenreQuery = "DELETE FROM book_genre WHERE book_id = ?";
        String insertBookGenreQuery = "INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)";

        try (Connection resourceConnection = connectionManager.getConnection();
             PreparedStatement updateBookStatement = resourceConnection.prepareStatement(updateBookQuery);
             PreparedStatement deleteBookGenreStatement = resourceConnection.prepareStatement(deleteBookGenreQuery);
             PreparedStatement insertBookGenreStatement = resourceConnection.prepareStatement(insertBookGenreQuery)) {

            updateBookStatement.setInt(1, authorId);
            updateBookStatement.setInt(2, book.getId());
            updateBookStatement.executeUpdate();

            deleteBookGenreStatement.setInt(1, book.getId());
            deleteBookGenreStatement.executeUpdate();

            for (int genreId : genresId) {
                insertBookGenreStatement.setInt(1, book.getId());
                insertBookGenreStatement.setInt(2, genreId);
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
    public List<Book> getBooksByAuthorId(int id) {
        List<Book> books = new ArrayList<>();
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT * FROM 'book' WHERE 'author_id' = " + id + ";";
            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                String message = "Книга с id " + id + " не найдена";
                throw new NoSuchEntityException(id, message);
            }

            while (resultSet.next()) {
                Book book = resultSetMapper.map(resultSet);
                books.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return books;
    }

    @Override
    public List<Book> getBooksByGenreId(int id) {
        List<Book> books = new ArrayList<>();
        try {
            connection = connectionManager.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT b.id, b.name, b.price, b.author_id" +
                    "FROM book b" +
                    "JOIN book_genre bg ON b.id = bg.book_id " +
                    "WHERE bg.genre_id = " + id + ";";
            ResultSet resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                String message = "Жанр с id " + id + " не найден";
                throw new NoSuchEntityException(id, message);
            }

            while (resultSet.next()) {
                Book book = resultSetMapper.map(resultSet);
                books.add(book);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionManager.closeConnection(resultSet, statement, connection);
        }
        return books;
    }

}


