package repository;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.example.rest.repository.impl.RepositoryMapperStorage;
import comon.TestSetup;
import db.impl.ConnectionManagerImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryTest extends TestSetup {

    BookRepositoryImpl bookRepository = RepositoryMapperStorage.getBookRepository();


    @BeforeAll
    static void setup() {
        TestbaseSetup testbaseSetup = new TestbaseSetup();
        testbaseSetup.createBases();
    }


    @Test
    void addTest() throws IllegalArgumentException, SQLException {
        bookRepository.add(book1);
        int id = 0;
        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();
        Connection connection = connectionManager.getConnection();
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
    void findByIdTest() throws NoSuchEntityException {
        Book gettedBook = bookRepository.findById(2);
        assertNotNull(gettedBook);

    }

    @Test
    void deleteByIdTest() throws NoSuchEntityException {
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
        Book book = new Book();
        book.setId(2);
        book.setName("Новое название");
        book.setPrice(850);
        bookRepository.update(book);
        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();
        Connection connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM book WHERE 'id' = 2;");
        String genreName = "";

        if (resultSet.next()) {
            genreName = resultSet.getString("name");
            assertEquals(genreName, book1.getName());

        }
    }
}
