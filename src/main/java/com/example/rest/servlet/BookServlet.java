package com.example.rest.servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.example.rest.dto.BookIncomingDTO;
import com.example.rest.repository.impl.BookRepositoryImpl;
import com.example.rest.service.BookService;
import db.impl.ConnectionManagerImpl;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "bookServlet", value = "/book")
public class BookServlet extends HttpServlet implements DefaultServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter("id");
        BookService bookService = createBookService();
        String bookJson = null;
        try {
            if (stringId != null) {
                int id = Integer.parseInt(stringId);
                bookJson = bookService.findById(id);
            } else {
                sendErrorResponse(response, "Укажите Id нужной вам книги");
                return;
            }
            sendResponse(response, bookJson);
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }
    }


    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter("id");
        BookService bookService = createBookService();
        boolean isDeleted = false;
        try {
            if (stringId != null) {
                int id = Integer.parseInt(stringId);
                isDeleted = bookService.delete(id);
            } else {
                sendErrorResponse(response, "Укажите Id книги, которую хотите удалить");
                return;
            }

            if (isDeleted) {
                response.getWriter().println("Книга успешно удалена. \n" +
                        "Есть преступления хуже, чем удалять книги. Например — не читать их");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().flush();
            } else {
                response.getWriter().println("Произошла ошибка на сервере");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().flush();
            }
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalArgumentException, NumberFormatException {
        BookService service = createBookService();
        String action = request.getParameter("action");
        BookIncomingDTO dto = null;
        boolean hasRelations = request.getParameter("author_id").isEmpty();
        try {
            if (!hasRelations) {
                dto = createRelationsDto(request);
            } else {
                dto = createDtoWithoutRelations(request);
            }
            boolean result = action.equals("add") ? service.add(dto) : service.update(dto);

            if (result) {
                response.getWriter().println("Операция прошла успешно");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().flush();
            } else {
                response.getWriter().println("Произошла ошибка на сервере");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().flush();
            }
        } catch (Exception e) {
            sendErrorResponse(response, "Введены неверные данные " + e.getMessage());
        }
    }


    private BookIncomingDTO createDtoWithoutRelations(HttpServletRequest request) {
        BookIncomingDTO dto = new BookIncomingDTO();
        dto.setId(Integer.parseInt(request.getParameter("id")));
        dto.setName(request.getParameter("name"));
        dto.setPrice(Integer.parseInt(request.getParameter("price")));
        return dto;
    }

    private BookIncomingDTO createRelationsDto(HttpServletRequest request) {
        BookIncomingDTO dto = createDtoWithoutRelations(request);
        dto.setAuthorId(Integer.parseInt(request.getParameter("author_id")));

        String[] genreIds = request.getParameterValues("genresId");
        List<Integer> genresId = new ArrayList<>();
        for (String genre : genreIds) {
            int id = Integer.parseInt(genre);
            genresId.add(id);
        }
        dto.setGenresId(genresId);
        return dto;
    }

    protected BookService createBookService() {
        BookService bookService = null;
        bookService = new BookService(new BookRepositoryImpl(new ConnectionManagerImpl()));
        return bookService;
    }
}