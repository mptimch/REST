package com.example.rest;

import db.impl.ConnectionManagerImpl;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {

        ConnectionManagerImpl connectionManager = new ConnectionManagerImpl();
        try {
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();

            String[] sqlFiles = {"dropTables.sql", "createAuthors.sql", "createBooks.sql",
                    "createGenres.sql", "createBook_Genre.sql"};

            for (String filename : sqlFiles) {
                executeScript(filename, statement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void executeScript(String filename, Statement statement) throws IOException, SQLException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder script = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                script.append(line).append("\n");
                if (line.endsWith(";")) {
                    script.setLength(script.length() - 1);
                    statement.execute(script.toString());
                    script.setLength(0);
                }
            }
        }
    }
}
