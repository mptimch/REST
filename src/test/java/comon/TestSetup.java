package comon;

import com.example.rest.model.Author;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;

import java.io.PrintWriter;
import java.util.List;

import static org.mockito.Mockito.mock;

public class TestSetup {

    public static HttpServletRequest request = mock(HttpServletRequest.class);
    public static HttpServletResponse response = mock(HttpServletResponse.class);
    public static PrintWriter writer = mock(PrintWriter.class);

    public static Author author1;
    public static Author author2;
    public static Book book1;
    public static Book book2;
    public static Book book3;
    public static Genre genre1;
    public static Genre genre2;
    public static Genre genre3;


    @BeforeAll
    static void setUp() {
        author1 = new Author(10, "Лев Толстой");
        author2 = new Author(200, "Стивен Кинг");
        book1 = new Book(1, "Война и мир", 1867, author1);
        book2 = new Book(60, "Темная башня", 2300, author2);
        book3 = new Book(182, "Анна Каренина", 1000, author1);
        genre1 = new Genre(1, "Классическая литература");
        genre2 = new Genre(3, "Роман");
        genre3 = new Genre(11, "Ужасы");

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

    }
}
