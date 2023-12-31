package repository;

import com.example.rest.Main;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import comon.MySQLTestContainer;
import comon.TestSetup;
import db.impl.ConnectionManagerImpl;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class TestbaseSetup extends TestSetup {

    String testDBurl = MySQLTestContainer.mySQLContainer.getJdbcUrl();

    ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(testDBurl, "mptimchenko", "12341990");

    private String createAuthorTable = "CREATE TABLE author (id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(255));";
    private String createBookTable = "CREATE TABLE book (id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(255), price INT, author_id INT, CONSTRAINT author_id_key FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE);";
    private String createGenreTable = "CREATE TABLE genre (id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(255));";
    private String createBookGenreTable = "CREATE TABLE book_genre (book_id INT, genre_id INT, PRIMARY KEY (book_id, genre_id), FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE, FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE);";
    private String dropTableFile = "dropTables.sql";
    private List<String> createTables = List.of(createAuthorTable, createBookTable, createGenreTable, createBookGenreTable);

    public void createBases() {
        try {
            Connection connection = MySQLTestContainer.getConnection();
            Statement statement = connection.createStatement();
            Main.executeScript(dropTableFile, statement);

            for (String query : createTables) {
                Statement newStatement = connection.createStatement();
                newStatement.execute(query);
                newStatement.close();
            }
            statement.close();

            GenreRepositoryImpl genreRepository = new GenreRepositoryImpl(connectionManager);
            BookRepositoryImpl bookRepository = new BookRepositoryImpl(connectionManager);
            AuthorRepositoryImpl authorRepository = new AuthorRepositoryImpl(connectionManager);
            authorRepository.add(author1);
            authorRepository.add(author2);

            List <Book> books = List.of(book1, book2, book3);
            for (Book book : books) {
                bookRepository.add(book);
            }

            List <Genre> genres = List.of(genre1, genre2, genre3);
            for (Genre genre4 : genres) {
                genreRepository.add(genre4);
            }

            for (Book book : books) {
                List <Genre> genreList = book.getGenres();
                List <Integer> genresId = genreList.stream().map(Genre::getId).collect(Collectors.toList());
                bookRepository.update(book, book.getAuthor().getId(), genresId);
            }
            for (Genre genre : genres) {
                List <Book> bookList = genre.getBooks();
                List <Integer> booksId = bookList.stream().map(Book::getId).collect(Collectors.toList());
                genreRepository.update(genre, booksId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
