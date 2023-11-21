package com.example.rest.servlet;

import com.example.rest.dto.GenreIncomingDTO;
import com.example.rest.service.Genreservice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DefaultServlet {

    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException;

    void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException;

    default void sendResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        response.setStatus(HttpServletResponse.SC_OK);
    }


    default void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.getWriter().println(message);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().flush();
    }





}
