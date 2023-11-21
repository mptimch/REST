package service;

import com.example.rest.dto.GenreIncomingDTO;
import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import com.example.rest.service.Genreservice;
import comon.TestSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenreServiceTest extends TestSetup {

    @Mock
    private GenreRepositoryImpl genreRepository = Mockito.mock(GenreRepositoryImpl.class);

    @InjectMocks
    private Genreservice genreservice = new Genreservice(genreRepository);


    @Test
    void findByIdNormalTest() throws SQLException, NoSuchEntityException {
        Mockito.when(genreRepository.findById(3)).thenReturn(genre2);
        String equals = genreservice.findById(3);
        String expected = "{\"name\":\"Роман\",\"books\":[\"Война и мир\",\"Темная башня\"]}";
        assertEquals(expected, equals);
    }

    @Test
    void findByIdExceptionTest() throws SQLException, NoSuchEntityException {
        Mockito.when(genreRepository.findById(299)).thenThrow(NoSuchEntityException.class);
        assertThrows(NoSuchEntityException.class, () -> genreservice.findById(299));
    }

    @ParameterizedTest
    @CsvSource({ // параметры: количество элементов, индекс элемента для вставки
            "11, true",
            "999, false"
    })
    void deleteTest(int id, boolean result) throws SQLException, NoSuchEntityException {
        Mockito.when(genreRepository.deleteById(11)).thenReturn(true);
        Mockito.when(genreRepository.deleteById(999)).thenReturn(false);

        boolean isDeleted = genreservice.delete(id);
        assertEquals(isDeleted, result);
    }

    @Test
    void addTestNormal() throws SQLException {
        GenreIncomingDTO dto = createDTO(genre1);
        dto.setBooksId(null);
        Mockito.when(genreRepository.add(Mockito.any())).thenReturn(true);
        boolean added = genreservice.add(dto);
        assertEquals(added, true);
    }


    @Test
    void addTestException() throws SQLException {
        GenreIncomingDTO dto = createDTO(genre2);
        dto.setBooksId(null);
        Mockito.when(genreRepository.add(Mockito.any())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> genreservice.add(dto));
    }


    @Test
    void updateTestNormal() throws SQLException, NoSuchEntityException {
        GenreIncomingDTO dto = createDTO(genre3);
        Mockito.when(genreRepository.update(Mockito.any(Genre.class),
                Mockito.anyList())).thenReturn(true);
        boolean added = genreservice.update(dto);
        assertEquals(added, true);
    }


    @Test
    void updateTestException() throws SQLException, NoSuchEntityException {
        GenreIncomingDTO dto = createDTO(genre3);
        Mockito.when(genreRepository.update(Mockito.any(Genre.class),
                Mockito.anyList())).thenThrow(NoSuchEntityException.class);

        assertThrows(NoSuchEntityException.class, () -> genreservice.update(dto));
    }

    private GenreIncomingDTO createDTO(Genre genre) {
        GenreIncomingDTO dto = new GenreIncomingDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        List<Book> books = genre.getBooks();
        List<Integer> booksId = books.stream().map(Book::getId).collect(Collectors.toList());
        dto.setBooksId(booksId);
        return dto;
    }
}
