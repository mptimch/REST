package servlet;

import com.example.rest.dto.AuthorIncomingDTO;
import com.example.rest.exceptions.NoSuchEntityException;
import com.example.rest.service.AuthorService;
import com.example.rest.servlet.AuthorServlet;
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

class AuthorServletTest extends TestSetup {


    @Mock
    private AuthorService authorService = mock(AuthorService.class);


    AuthorServlet servlet = new AuthorServlet() {
        @Override
        public AuthorService createAuthorService() {
            return authorService;
        }
    };

    HttpServletRequest request;
    HttpServletResponse response;
    PrintWriter writer;

    @BeforeEach
    public void setUp() {
        getServletsAndWriter();
    }


    @Test
    void doGetTest() throws Exception {

        when(response.getWriter()).thenReturn(writer);
        String stringDTO = "{\"name\":\"Лев Толстой\",\"books\":[\"Война и мир\",\"Анна Каренина\"]}";
        when(request.getParameter("id")).thenReturn("10");
        when(authorService.findById(10)).thenReturn(stringDTO);

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
        when(authorService.delete(5)).thenReturn(true);

        servlet.doDelete(request, response);

        Mockito.verify(writer).println("Автор успешно удален");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }

    @Test
    void doDeleteTestWrong() throws IOException, SQLException, NoSuchEntityException {

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("id")).thenReturn(null);

        servlet.doDelete(request, response);

        Mockito.verify(writer).println("Укажите Id автора, которого хотите удалить");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }

    @Test
    void doAddTestException() throws IOException, SQLException, NoSuchEntityException {

        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("id")).thenReturn("Максим Горький");
        when(request.getParameter("name")).thenReturn("14");

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Введены неверные данные For input string: \"Максим Горький\"");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }

    @Test
    void doAddTest() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Максим Горький");
        AuthorIncomingDTO dto = new AuthorIncomingDTO();
        dto.setName("Максим Горький");
        dto.setId(14);

        when(authorService.add(Mockito.any())).thenReturn(true);

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Операция прошла успешно");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }

    @Test
    void doUpdateTestException() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Максим Горький");

        when(authorService.update(Mockito.any())).thenThrow(new NoSuchEntityException(14, "Пользователь с id" + 14 + " не найден"));


        servlet.doPost(request, response);

        Mockito.verify(writer).println("Введены неверные данные Пользователь с id14 не найден");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(writer).flush();
    }


    @Test
    void doAUpdatetTest() throws IOException, SQLException, NoSuchEntityException {
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("14");
        when(request.getParameter("name")).thenReturn("Максим Горький");
        AuthorIncomingDTO dto = new AuthorIncomingDTO();
        dto.setName("Максим Горький");
        dto.setId(14);

        when(authorService.update(Mockito.any())).thenReturn(true);

        servlet.doPost(request, response);

        Mockito.verify(writer).println("Операция прошла успешно");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(writer).flush();
    }

    private void getServletsAndWriter() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
    }
}
