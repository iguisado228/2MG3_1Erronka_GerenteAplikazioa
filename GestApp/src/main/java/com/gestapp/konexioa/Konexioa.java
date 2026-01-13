package com.gestapp.konexioa;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Konexioa {

    private static final String URL = "jdbc:mysql://192.168.1.112:3306/2mg3_1erronka";
    private static final String USER = "3Taldea";
    private static final String PASSWORD = "2MG3_3Taldea3";

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

