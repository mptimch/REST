package com.example.rest.servlet;

import com.example.rest.dto.AuthorIncomingDTO;
import com.example.rest.repository.impl.AuthorRepositoryImpl;
import com.example.rest.service.AuthorService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "authorServlet", value = "/author")
public class AuthorServlet extends HttpServlet implements DefaultServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter("id");
        AuthorService authorService = createAuthorService();
        String bookJson = null;
        try {
            if (stringId != null) {
                int id = Integer.parseInt(stringId);
                bookJson = authorService.findById(id);
            } else {
                sendErrorResponse(response, "Укажите Id нужного автора");
            }
            sendResponse(response, bookJson);
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter("id");
        AuthorService authorService = createAuthorService();
        boolean isDeleted = false;
        try {
            if (stringId != null) {
                int id = Integer.parseInt(stringId);
                isDeleted = authorService.delete(id);
            } else {
                sendErrorResponse(response, "Укажите Id автора, которого хотите удалить");
                return;
            }

            if (isDeleted) {
                response.getWriter().println("Автор успешно удален");
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthorService authorService = createAuthorService();
        String action = request.getParameter("action");
        AuthorIncomingDTO dto = new AuthorIncomingDTO();
        try {
            dto.setId(Integer.parseInt(request.getParameter("id")));
            dto.setName(request.getParameter("name"));
            boolean result = action.equals("add") ? authorService.add(dto) : authorService.update(dto);

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

    public AuthorService createAuthorService() {
        AuthorService service = null;
        try {
            service = new AuthorService(new AuthorRepositoryImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }
}
