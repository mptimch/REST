package repository;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.repository.impl.BookRepositoryImpl;
import comon.MySQLTestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryTest extends MySQLTestContainer {

    BookRepositoryImpl bookRepository = new BookRepositoryImpl();

    @BeforeAll
    static void setup() {
        TestbaseSetup testbaseSetup = new TestbaseSetup();
        testbaseSetup.createBases();
    }
    @Test
    void addTest() throws IllegalArgumentException, SQLException {
        Connection connection = MySQLTestContainer.getConnection();
        bookRepository.add(book1, connection);
        int id = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(id) AS max_id FROM book;");

        if (resultSet.next()) {
            int maxId = resultSet.getInt("max_id");
            id = resultSet.getInt("max_id");
        }

        resultSet = statement.executeQuery("SELECT * FROM book WHERE 'id' = " + id + ";");
        String bookName = "";
        int gettedId = 0;

        if (resultSet.next()) {
            bookName = resultSet.getString("name");
            gettedId = resultSet.getInt("id");

            assertEquals(id, 1);
            assertEquals(bookName, author1.getName());
        }
    }

    @Test
    void findByIdTest() throws NoSuchEntityException, SQLException {
        Connection connection = MySQLTestContainer.getConnection();
        Book gettedBook = bookRepository.findById(2, connection);
        assertNotNull(gettedBook);
    }

    @Test
    void deleteByIdTest() throws NoSuchEntityException, SQLException {
        Connection connection = MySQLTestContainer.getConnection();
        Book book = bookRepository.findById(1, connection);
        bookRepository.deleteById(1, connection);
        assertThrows(NoSuchEntityException.class, () -> bookRepository.findById(1, connection));
    }

    @Test
    void updateTestException() throws IllegalArgumentException, SQLException {
        Connection connection = MySQLTestContainer.getConnection();
        book2.setId(54);
        assertThrows(NoSuchEntityException.class, () -> bookRepository.update(book2, connection));
    }

    @Test
    void updateTest() throws IllegalArgumentException, SQLException, NoSuchEntityException {
        Connection connection = MySQLTestContainer.getConnection();
        Book book = new Book();
        book.setId(2);
        book.setName("Новое название");
        book.setPrice(850);
        bookRepository.update(book, connection);
        Statement statement = MySQLTestContainer.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM book WHERE 'id' = 2;");
        String genreName = "";

        if (resultSet.next()) {
            genreName = resultSet.getString("name");
            assertEquals(genreName, book1.getName());
        }
        resultSet.close();
        statement.close();
    }
}
