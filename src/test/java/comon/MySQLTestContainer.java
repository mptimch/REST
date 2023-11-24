package comon;

import com.example.rest.model.Author;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import repository.TestbaseSetup;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Testcontainers
public class MySQLTestContainer extends TestbaseSetup {

    public static Author author1;
    public static Author author2;
    public static Book book1;
    public static Book book2;
    public static Book book3;
    public static Genre genre1;
    public static Genre genre2;
    public static Genre genre3;

    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("aston_rest")
            .withUsername("mptimchenko")
            .withPassword("12341990").withReuse(false);


    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
        generateEntities();
    }


    static void generateEntities() {
        author1 = new Author(1, "Лев Толстой");
        author2 = new Author(2, "Стивен Кинг");
        book1 = new Book(1, "Война и мир", 1867, author1);
        book2 = new Book(2, "Темная башня", 2300, author2);
        book3 = new Book(3, "Анна Каренина", 1000, author1);
        genre1 = new Genre(1, "Классическая литература");
        genre2 = new Genre(2, "Роман");
        genre3 = new Genre(3, "Ужасы");

        author1.setBooks(List.of(book1, book3));
        author2.setBooks(List.of(book2));

        book1.setAuthor(author1);
        book3.setAuthor(author1);
        book2.setAuthor(author2);
        book1.setGenres(List.of(genre1, genre2));
        book2.setGenres(List.of(genre2, genre3));
        book3.setGenres(List.of(genre2));

        genre1.setBooks(List.of(book1));
        genre2.setBooks(List.of(book1, book2));
        genre3.setBooks(List.of(book3));

        TestbaseSetup testbaseSetup = new TestbaseSetup();
        testbaseSetup.createBases();

    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    @Test
    void testCurrentDateNotNull() throws Exception {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT CURRENT_DATE");
            assertNotNull(resultSet);
            resultSet.next();
            LocalDate currentDate = resultSet.getObject(1, LocalDate.class);
            System.out.println("Date is " + currentDate);
            assertNotNull(currentDate);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(mySQLContainer.getJdbcUrl(), mySQLContainer.getUsername(), mySQLContainer.getPassword());
    }
}
