package servlet;

import com.example.rest.dto.AuthorIncomingDTO;
import com.example.rest.dto.BookIncomingDTO;
import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.service.AuthorService;
import com.example.rest.service.BookService;
import com.example.rest.servlet.AuthorServlet;
import com.example.rest.servlet.BookServlet;
import comon.TestSetup;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookServletTest extends TestSetup {

    @Mock
    private BookService bookService = mock(BookService.class);


    BookServlet servlet = new BookServlet() {
        @Override
        protected BookService createBookService() {
            return bookService;
        }
    };


    @Test
    public void doGetTest() throws IOException, NoSuchEntityException, SQLException {
        when(response.getWriter()).thenReturn(writer);
        String stringDTO = "{\"name\":\"Война и мир\"," +
                "\"price\":1867," +
                "\"author\":\"Лев Толстой\"," +
                "\"genres\":[\"Классическая литература\",\"Роман\"]}";
        when(request.getParameter("id")).thenReturn("10");
        when(bookService.findById(10)).thenReturn(stringDTO);

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).print(stringDTO);
        Mockito.verify(writer).flush();
    }

    @Test
    public void doDeleteTest() throws IOException, SQLException, NoSuchEntityException {

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn("5");
        when(bookService.delete(5)).thenReturn(true);

        servlet.doDelete(request, response);

        Mockito.verify(writer).println("Книга успешно удалена. \n" +
                "Есть преступления хуже, чем удалять книги. Например — не читать их");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }

    @Test
    public void doDeleteTestWrong() throws IOException, SQLException, NoSuchEntityException {

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn(null);

        servlet.doDelete(request, response);

        Mockito.verify(writer).println("Укажите Id книги, которую хотите удалить");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }

    @Test
    public void doAddTestException() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("id")).thenReturn("Игра престолов");
        when(request.getParameter("name")).thenReturn("14");
        when(request.getParameter("author")).thenReturn("");


        servlet.doPost(request, response);

        Mockito.verify(writer).println("Введены неверные данные For input string: \"Максим Горький\"");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }

    @Test
    public void doAddTestFullEntity() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Игра престолов");
        when(request.getParameter("price")).thenReturn("1400");
        when(request.getParameter("author_id")).thenReturn("5");
        when(request.getParameterValues("genresId")).thenReturn(new String[]{"1", "2"});

        when(bookService.add(Mockito.any())).thenReturn(true);

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Операция прошла успешно");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }

    @Test
    public void doUpdateTestShortEntity() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Игра престолов");
        when(request.getParameter("price")).thenReturn("1400");
        when(request.getParameter("author_id")).thenReturn("");



        when(bookService.update(Mockito.any())).thenReturn(true);

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Операция прошла успешно");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }


    @Test
    public void doAUpdatetTestException() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Игра престолов");
        when(request.getParameter("price")).thenReturn("1400");
        when(request.getParameter("author_id")).thenReturn("");

        when(bookService.update(Mockito.any())).thenThrow(new NoSuchEntityException(14, "Книга с id" + 14 + " не найдена"));

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Введены неверные данные Книга с id14 не найдена");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }
}

