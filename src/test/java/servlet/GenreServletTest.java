package servlet;

import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.service.GenreService;
import com.example.rest.servlet.GenreServlet;
import comon.TestSetup;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreServletTest extends TestSetup {

    @Mock
    private GenreService genreservice = mock(GenreService.class);

    HttpServletRequest request;
    HttpServletResponse response;
    PrintWriter writer;

    @BeforeEach
    void setUp() {
        getServletsAndWriter();
    }


    GenreServlet servlet = new GenreServlet() {
        @Override
        protected GenreService createGenreService() {
            return genreservice;
        }
    };


    @Test
    void doGetTest() throws IOException, NoSuchEntityException, SQLException {
        when(response.getWriter()).thenReturn(writer);
        String stringDTO = "{\"name\":\"Роман\",\"books\":[\"Война и мир\",\"Темная башня\"]}";
        when(request.getParameter("id")).thenReturn("3");
        when(genreservice.findById(3)).thenReturn(stringDTO);

        servlet.doGet(request, response);

        Mockito.verify(response).setContentType("application/json");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).print(stringDTO);
        Mockito.verify(writer).flush();
    }

    @Test
    void doDeleteTest() throws IOException, SQLException, NoSuchEntityException {

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn("5");
        when(genreservice.delete(5)).thenReturn(true);

        servlet.doDelete(request, response);

        Mockito.verify(writer).println("Жанр успешно удален");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }

    @Test
    void doDeleteTestWrong() throws IOException, SQLException, NoSuchEntityException {

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn(null);

        servlet.doDelete(request, response);

        Mockito.verify(writer).println("Укажите Id жанра, который хотите удалить");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }

    @Test
    void doAddTestException() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("id")).thenReturn("Научная фантастика");
        when(request.getParameter("name")).thenReturn("14");
        when(request.getParameter("booksId")).thenReturn("");

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Введены неверные данные For input string: \"Научная фантастика\"");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }

    @Test
    void doAddTestFullEntity() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Научная фантастика");
        when(request.getParameter("booksId")).thenReturn("5");
        when(request.getParameterValues("booksId")).thenReturn(new String[]{"1", "2"});

        when(genreservice.add(Mockito.any())).thenReturn(true);

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Операция прошла успешно");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }

    @Test
    void doUpdateTestShortEntity() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Научная фантастика");
        when(request.getParameter("booksId")).thenReturn("");

        when(genreservice.update(Mockito.any())).thenReturn(true);

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Операция прошла успешно");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }


    @Test
    void doAUpdatetTestException() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Научная фантастика");
        when(request.getParameter("price")).thenReturn("1400");
        when(request.getParameter("booksId")).thenReturn("");

        when(genreservice.update(Mockito.any())).thenThrow(new NoSuchEntityException(14, "Жанр с id " + 14 + " не найден"));

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Введены неверные данные Жанр с id 14 не найден");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }

    private void getServletsAndWriter() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
    }
}
