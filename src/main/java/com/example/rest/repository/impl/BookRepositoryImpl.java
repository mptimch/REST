package com.example.rest.repository.impl;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.repository.BookRepository;
import com.example.rest.repository.mapper.BookResultSetMapperImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    @Override
    public Book findById(Integer id, Connection connection) throws NoSuchEntityException, SQLException {
        Book book = new Book();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE id = " + id + ";")) {

            if (!resultSet.isBeforeFirst()) {
                String message = "Книга с id " + id + " не найдена";
                throw new NoSuchEntityException(id, message);
            }

            book = new BookResultSetMapperImpl(connection).map(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return book;
    }

    @Override
    public boolean deleteById(Integer id, Connection connection) throws SQLException {
        boolean succsess = false;
        try (Statement statement = connection.createStatement()) {
            succsess = statement.execute("DELETE FROM book WHERE id = " + id + ";");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return succsess;
    }

    @Override
    public boolean add(Book book, Connection connection) throws IllegalArgumentException, SQLException {
        boolean result = false;
        String sql = "INSERT INTO book (name, price) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setInt(2, book.getPrice());
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

    public boolean add(Book book, int authorId, List<Integer> genresId, Connection connection) throws IllegalArgumentException, SQLException {
        boolean result = false;
        String name = book.getName();
        String sqlToBook = "INSERT INTO book (name, price, author_id) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlToBook, Statement.RETURN_GENERATED_KEYS)) {
            int bookId = 0;
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
            insertBookGenreStatement.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(Book book, Connection connection) throws NoSuchEntityException, SQLException {
        boolean success = false;
        String sql = "UPDATE book SET name = ?, price = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            findById(book.getId(), connection);
            statement.setString(1, book.getName());
            statement.setInt(2, book.getPrice());
            statement.setInt(3, book.getId());
            int rowsUpdated = statement.executeUpdate();
            success = rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }


    public boolean update(Book book, int authorId, List<Integer> genresId, Connection connection) throws NoSuchEntityException, SQLException {
        update(book, connection);
        boolean success = false;
        String updateBookQuery = "UPDATE book SET author_id = ? WHERE id = ?";
        String deleteBookGenreQuery = "DELETE FROM book_genre WHERE book_id = ?";
        String insertBookGenreQuery = "INSERT INTO book_genre (book_id, genre_id) VALUES (?, ?)";

        try (PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery);
             PreparedStatement deleteBookGenreStatement = connection.prepareStatement(deleteBookGenreQuery);
             PreparedStatement insertBookGenreStatement = connection.prepareStatement(insertBookGenreQuery)) {

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
    public List<Book> getBooksByAuthorId(int id, Connection connection) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE author_id = " + id + ";";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (!resultSet.isBeforeFirst()) {
                String message = "Книга по id автора " + id + " не найдена";
                throw new NoSuchEntityException(id, message);
            }

            while (resultSet.next()) {
                Book book = new BookResultSetMapperImpl(connection).getBooksByAuthorId(resultSet);
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> getBooksByGenreId(int id, Connection connection) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.id, b.name, b.price, b.author_id " +
                "FROM book b " +
                "JOIN book_genre bg ON b.id = bg.book_id " +
                "WHERE bg.genre_id = " + id + ";";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            if (!resultSet.next()) {
                String message = "Жанр с id " + id + " не найден";
                throw new NoSuchEntityException(id, message);
            }

            while (resultSet.next()) {
                Book book = new BookResultSetMapperImpl(connection).getBooksByAuthorId(resultSet);
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
}


