package com.example.rest.servlet;

import com.example.rest.dto.GenreIncomingDTO;
import com.example.rest.repository.impl.GenreRepositoryImpl;
import com.example.rest.service.GenreService;
import db.impl.ConnectionManagerImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "genreServlet", value = "/genre")
public class GenreServlet extends HttpServlet implements DefaultServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter("id");
        GenreService genreservice = createGenreService();
        String genreJson = null;
        try {
            if (stringId != null) {
                int id = Integer.parseInt(stringId);
                genreJson = genreservice.findById(id);
            } else {
                sendErrorResponse(response, "Укажите Id нужного вам жанра");
            }

            sendResponse(response, genreJson);

        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stringId = request.getParameter("id");
        GenreService genreservice = createGenreService();
        boolean isDeleted = false;
        try {
            if (stringId != null) {
                int id = Integer.parseInt(stringId);
                isDeleted = genreservice.delete(id);
            } else {
                sendErrorResponse(response, "Укажите Id жанра, который хотите удалить");
                return;
            }

            if (isDeleted) {
                response.getWriter().println("Жанр успешно удален");
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


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GenreService service = createGenreService();
        String action = request.getParameter("action");
        GenreIncomingDTO dto = null;
        boolean hasRelations = request.getParameter("booksId").isEmpty();
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


    private GenreIncomingDTO createDtoWithoutRelations(HttpServletRequest request) {
        GenreIncomingDTO dto = new GenreIncomingDTO();
        dto.setId(Integer.parseInt(request.getParameter("id")));
        dto.setName(request.getParameter("name"));
        return dto;
    }

    private GenreIncomingDTO createRelationsDto(HttpServletRequest request) {
        GenreIncomingDTO dto = createDtoWithoutRelations(request);

        String[] bookIds = request.getParameterValues("booksId");
        List<Integer> booksId = new ArrayList<>();
        for (String book : bookIds) {
            int id = Integer.parseInt(book);
            booksId.add(id);
        }
        dto.setBooksId(booksId);
        return dto;
    }

    protected GenreService createGenreService() {
        GenreService genreservice = null;
        try {
            genreservice = new GenreService(new GenreRepositoryImpl(new ConnectionManagerImpl().getConnection()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return genreservice;
    }

}



