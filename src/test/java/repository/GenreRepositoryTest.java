package repository;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Genre;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import comon.MySQLTestContainer;
import db.impl.ConnectionManagerImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class GenreRepositoryTest extends MySQLTestContainer {
    String testDBurl = MySQLTestContainer.mySQLContainer.getJdbcUrl();
    ConnectionManagerImpl connectionManager = new ConnectionManagerImpl(testDBurl, "mptimchenko", "12341990");
    GenreRepositoryImpl genreRepository = new GenreRepositoryImpl(connectionManager);

    @BeforeAll
    static void setup() {
        TestbaseSetup testbaseSetup = new TestbaseSetup();
        testbaseSetup.createBases();
    }

    @Test
    void addTest() throws IllegalArgumentException, SQLException {
        Connection connection = connectionManager.getConnection();
        genreRepository.add(genre1);
        int id = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(id) AS max_id FROM genre;");

        if (resultSet.next()) {
            int maxId = resultSet.getInt("max_id");
            id = resultSet.getInt("max_id");
        }

        resultSet = statement.executeQuery("SELECT * FROM genre WHERE 'id' = " + id + ";");
        String genreName = "";
        int gettedId = 0;

        if (resultSet.next()) {
            genreName = resultSet.getString("name");
            gettedId = resultSet.getInt("id");

            assertEquals(id, 1);
            assertEquals(genreName, author1.getName());
        }
        ConnectionManagerImpl.closeConnection(connection, statement, resultSet);
    }

    @Test
    void findByIdTest() throws NoSuchEntityException, SQLException {
        Genre genre = genreRepository.findById(2);
        assertNotNull(genre);
    }

    @Test
    void deleteByIdTest() throws NoSuchEntityException, SQLException {
        genreRepository.deleteById(1);
        assertThrows(NoSuchEntityException.class, () -> genreRepository.findById(1));
    }

    @Test
    void updateTestException() throws IllegalArgumentException, SQLException {
        genre2.setId(54);
        assertThrows(NoSuchEntityException.class, () -> genreRepository.update(genre2));
    }

    @Test
    void updateTest() throws IllegalArgumentException, SQLException, NoSuchEntityException {
        Connection connection = connectionManager.getConnection();
        Genre genre = new Genre();
        genre.setId(2);
        genre.setName("Новый жанр");
        genreRepository.update(genre);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM genre WHERE 'id' = 2;");
        String genreName = "";

        if (resultSet.next()) {
            genreName = resultSet.getString("name");
            assertEquals(genreName, genre1.getName());
        }
        ConnectionManagerImpl.closeConnection(connection, statement, resultSet);
    }
}
