package repository;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Author;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
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

class AuthorRepositoryTest extends TestSetup {

    AuthorRepositoryImpl authorRepository = RepositoryMapperStorage.getAuthorRepository();


    @BeforeAll
    static void setup() {
        TestbaseSetup testbaseSetup = new TestbaseSetup();
        testbaseSetup.createBases();
    }


    @Test
    void addTest() throws IllegalArgumentException, SQLException {
        authorRepository.add(author1);
        int id = 0;
        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();
        Connection connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(id) AS max_id FROM author;");

        if (resultSet.next()) {
            int maxId = resultSet.getInt("max_id");
            id = resultSet.getInt("max_id");
        }

        resultSet = statement.executeQuery("SELECT * FROM author WHERE 'id' = " + id + ";");
        String authorName = "";
        int gettedId = 0;

        if (resultSet.next()) {
            authorName = resultSet.getString("name");
            gettedId = resultSet.getInt("id");

            assertEquals(id, 1);
            assertEquals(authorName, author1.getName());
        }
    }

    @Test
    void findByIdTest() throws NoSuchEntityException {
        Author gettedAuthor = authorRepository.findById(2);
        assertNotNull(gettedAuthor);

    }

    @Test
    void deleteByIdTest() throws NoSuchEntityException {
        Author author = authorRepository.findById(1);
        authorRepository.deleteById(1);
        assertThrows(NoSuchEntityException.class, () -> authorRepository.findById(1));
    }

    @Test
    void updateTestException() throws IllegalArgumentException, SQLException {
        author2.setId(54);
        assertThrows(NoSuchEntityException.class, () -> authorRepository.update(author2));
    }

    @Test
    void updateTest() throws IllegalArgumentException, SQLException, NoSuchEntityException {
        Author author = new Author();
        author.setId(2);
        author.setName("Новое имя");
        authorRepository.update(author);
        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();
        Connection connection = connectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM author WHERE 'id' = 2;");
        String authorName = "";

        if (resultSet.next()) {
            authorName = resultSet.getString("name");
            assertEquals(authorName, author1.getName());
        }
    }
}
