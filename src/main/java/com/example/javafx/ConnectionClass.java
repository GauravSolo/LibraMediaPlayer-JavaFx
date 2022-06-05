package com.example.javafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionClass {
    public  Connection connection;
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String dbName = "javafx";
        String username = "root";
        String pass = "12345";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, username, pass);
        }catch (Exception e)
        {
            System.out.println("jdbc error => "+ e);
            e.printStackTrace();
        }

return connection;
    }

}
