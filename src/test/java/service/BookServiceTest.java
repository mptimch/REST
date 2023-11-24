package service;

import com.example.rest.dto.BookIncomingDTO;
import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.example.rest.service.BookService;
import comon.TestSetup;
import db.impl.ConnectionManagerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class BookServiceTest extends TestSetup {

    @Mock
    private BookRepositoryImpl bookRepository = Mockito.mock(BookRepositoryImpl.class);

    @Mock
    ConnectionManagerImpl connectionManager = Mockito.mock(ConnectionManagerImpl.class);

    private BookService bookService = new BookService(bookRepository);

    @Test
    void findByIdNormalTest() throws SQLException, NoSuchEntityException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(bookRepository.findById(1)).thenReturn(book1);
        String equals = bookService.findById(1);
        String expected = "{\"name\":\"Война и мир\"," +
                "\"price\":1867," +
                "\"author\":\"Лев Толстой\"," +
                "\"genres\":[\"Классическая литература\",\"Роман\"]}";
        assertEquals(expected, equals);
    }

    @Test
    void findByIdExceptionTest() throws SQLException, NoSuchEntityException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(bookRepository.findById(299)).thenThrow(NoSuchEntityException.class);
        assertThrows(NoSuchEntityException.class, () -> bookService.findById(299));
    }

    @ParameterizedTest
    @CsvSource({ // параметры: количество элементов, индекс элемента для вставки
            "182, true",
            "999, false"
    })
    void deleteTest(int id, boolean result) throws SQLException, NoSuchEntityException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(bookRepository.deleteById(182)).thenReturn(true);
        Mockito.when(bookRepository.deleteById(999)).thenReturn(false);
        boolean isDeleted = bookService.delete(id);
        assertEquals(isDeleted, result);
    }

    @Test
    void addTestNormal() throws SQLException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        BookIncomingDTO dto = createDTO(book1);
        Mockito.when(bookRepository.add(Mockito.any(Book.class),
                Mockito.anyInt(), Mockito.anyList())).thenReturn(true);
        boolean added = bookService.add(dto);
        assertEquals(added, true);
    }


    @Test
    void addTestException() throws SQLException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        BookIncomingDTO dto = createDTO(book1);
        Mockito.when(bookRepository.add(Mockito.any(Book.class),
                Mockito.anyInt(), Mockito.anyList())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> bookService.add(dto));
    }


    @Test
    void updateTestNormal() throws SQLException, NoSuchEntityException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        Mockito.when(bookRepository.update(Mockito.any(Book.class))).thenReturn(true);
        BookIncomingDTO dto = createDTO(book3);
        dto.setGenresId(null);
        boolean added = bookService.update(dto);
        assertEquals(added, true);
    }


    @Test
    void updateTestException() throws SQLException, NoSuchEntityException {
        Mockito.when(connectionManager.getConnection()).thenReturn(null);
        BookIncomingDTO dto = createDTO(book3);
        dto.setGenresId(null);
        Mockito.when(bookRepository.update(Mockito.any())).thenThrow(NoSuchEntityException.class);
        assertThrows(NoSuchEntityException.class, () -> bookService.update(dto));
    }

    private BookIncomingDTO createDTO(Book book) {
        BookIncomingDTO dto = new BookIncomingDTO();
        dto.setId(book.getId());
        dto.setName(book.getName());
        dto.setPrice(book.getPrice());
        dto.setAuthorId(book.getAuthor().getId());
        List<Genre> genres = book.getGenres();
        List<Integer> genresId = genres.stream().map(Genre::getId).collect(Collectors.toList());
        dto.setGenresId(genresId);
        return dto;
    }
}
