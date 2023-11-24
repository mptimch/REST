package db.impl;

import db.ConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ConnectionManagerImpl implements ConnectionManager {

    String url;
    String user;
    String password;
    boolean isTestDataBase;

    public ConnectionManagerImpl() {
    }

    public ConnectionManagerImpl(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        isTestDataBase = true;
    }



    @Override
    public Connection getConnection() throws SQLException {
        if (isTestDataBase) {
            return DriverManager.getConnection(url, user, password);
        } else {
            getConnectionData();
            return DriverManager.getConnection(url, user, password);
        }
    }


    private void getConnectionData() {
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties");
            Properties properties = new Properties();
            properties.load(input);
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection (Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        connection.close();
        statement.close();
        if (resultSet != null) {
            resultSet.close();
        }
    }
}
