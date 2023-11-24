package service;

import com.example.rest.dto.AuthorIncomingDTO;
import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Author;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
import com.example.rest.service.AuthorService;
import comon.TestSetup;
import db.impl.ConnectionManagerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class AuthorServiceTest extends TestSetup {

    @Mock
    private AuthorRepositoryImpl authorRepository = Mockito.mock(AuthorRepositoryImpl.class);

    @Mock
    ConnectionManagerImpl connectionManager = Mockito.mock(ConnectionManagerImpl.class);

    private AuthorService authorService = new AuthorService(authorRepository, connectionManager);


    @Test
    void findByIdNormalTest() throws SQLException, NoSuchEntityException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(authorRepository.findById(10, connectionManager.getConnection())).thenReturn(author1);
        String equals = authorService.findById(10);
        String expected = "{\"name\":\"Лев Толстой\",\"books\":[\"Война и мир\",\"Анна Каренина\"]}";
        assertEquals(expected, equals);
    }

    @Test
    void findByIdExceptionTest() throws SQLException, NoSuchEntityException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(authorRepository.findById(299, connectionManager.getConnection())).thenThrow(NoSuchEntityException.class);
        assertThrows(NoSuchEntityException.class, () -> authorService.findById(299));
    }

    @ParameterizedTest
    @CsvSource({ // параметры: количество элементов, индекс элемента для вставки
            "10, true",
            "999, false"
    })
    void deleteTest(int id, boolean result) throws SQLException, NoSuchEntityException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(authorRepository.deleteById(10, connectionManager.getConnection())).thenReturn(true);
        Mockito.when(authorRepository.deleteById(999, connectionManager.getConnection())).thenReturn(false);
        boolean isDeleted = authorService.delete(id);
        assertEquals(isDeleted, result);
    }

    @Test
    void addTestNormal() throws SQLException {
        AuthorIncomingDTO dto = createDTO(author2);
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(authorRepository.add(Mockito.any(), Mockito.any())).thenReturn(true);
        boolean added = authorService.add(dto);
        assertEquals(added, true);
    }


    @Test
    void addTestException() throws SQLException {
        AuthorIncomingDTO dto = createDTO(author2);
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(authorRepository.add(Mockito.any(), Mockito.any())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> authorService.add(dto));
    }


    @Test
    void updateTestNormal() throws SQLException, NoSuchEntityException {
        AuthorIncomingDTO dto = createDTO(author1);
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(authorRepository.update(Mockito.any(), Mockito.any())).thenReturn(true);
        boolean added = authorService.update(dto);
        assertEquals(added, true);
    }


    @Test
    void updateTestException() throws SQLException, NoSuchEntityException {
        AuthorIncomingDTO dto = createDTO(author1);
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(authorRepository.update(Mockito.any(), Mockito.any())).thenThrow(NoSuchEntityException.class);
        assertThrows(NoSuchEntityException.class, () -> authorService.update(dto));
    }

    private AuthorIncomingDTO createDTO(Author author) {
        AuthorIncomingDTO dto = new AuthorIncomingDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        return dto;
    }
}
