package comon;

import com.example.rest.model.Author;
import com.example.rest.model.Book;
import com.example.rest.model.Genre;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import db.impl.ConnectionManagerImpl;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;


public class TestSetup {
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
    }
}
