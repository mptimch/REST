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


    @Override
    public Connection getConnection() throws SQLException {
        getConnectionData();
        return DriverManager.getConnection(url, user, password);
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



    public void closeConnection(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
