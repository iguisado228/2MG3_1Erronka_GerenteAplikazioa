package com.gestapp.konexioa;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Konexioa {

    private static final String URL = "jdbc:mysql://localhost:3306/erronka";
    private static final String USER = "root";
    private static final String PASSWORD = "1MG2024";

    //public static Connection getConnection() throws SQLException {
      //  return DriverManager.getConnection(URL, USER, PASSWORD);
   // }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }
}

