package repository;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.repository.impl.BookRepositoryImpl;
import comon.MySQLTestContainer;
import db.impl.ConnectionManagerImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryTest extends MySQLTestContainer {
    String testDBurl = MySQLTestContainer.mySQLContainer.getJdbcUrl();
    ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(testDBurl, "mptimchenko", "12341990");
    BookRepositoryImpl bookRepository = new BookRepositoryImpl(connectionManager);

    @BeforeAll
    static void setup() {
        TestbaseSetup testbaseSetup = new TestbaseSetup();
        testbaseSetup.createBases();
    }
    @Test
    void addTest() throws IllegalArgumentException, SQLException {
        Connection connection = connectionManager.getConnection();
        bookRepository.add(book1);
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
        ConnectionManagerImpl.closeConnection(connection, statement, resultSet);
    }

    @Test
    void findByIdTest() throws NoSuchEntityException, SQLException {
        Book gettedBook = bookRepository.findById(2);
        assertNotNull(gettedBook);
    }

    @Test
    void deleteByIdTest() throws NoSuchEntityException, SQLException {
        Book book = bookRepository.findById(1);
        bookRepository.deleteById(1);
        assertThrows(NoSuchEntityException.class, () -> bookRepository.findById(1));
    }

    @Test
    void updateTestException() throws IllegalArgumentException, SQLException {
        book2.setId(54);
        assertThrows(NoSuchEntityException.class, () -> bookRepository.update(book2));
    }

    @Test
    void updateTest() throws IllegalArgumentException, SQLException, NoSuchEntityException {
        Connection connection = connectionManager.getConnection();
        Book book = new Book();
        book.setId(2);
        book.setName("Новое название");
        book.setPrice(850);
        bookRepository.update(book);
        Statement statement = MySQLTestContainer.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM book WHERE 'id' = 2;");
        String genreName = "";

        if (resultSet.next()) {
            genreName = resultSet.getString("name");
            assertEquals(genreName, book1.getName());
        }
        ConnectionManagerImpl.closeConnection(connection, statement, resultSet);
    }
}
