package com.gestapp.controllers;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class konexioa {

    private static final String URL = "jdbc:mysql://192.168.115.155:3306/2mg3_1erronka";
    private static final String USER = "3taldea";
    private static final String PASSWORD = "1234";

    //public static Connection getConnection() throws SQLException {
      //  return DriverManager.getConnection(URL, USER, PASSWORD);
   // }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Konexioa egina");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }
}

