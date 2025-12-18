package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProduktuakGehituController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtPrezioa;
    @FXML private TextField txtMota;
    @FXML private TextField txtStock;

    @FXML
    private void gorde() {
        String sql = "INSERT INTO produktuak (izena, prezioa, mota, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtIzena.getText());
            pstmt.setDouble(2, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(3, txtMota.getText());
            pstmt.setInt(4, Integer.parseInt(txtStock.getText()));

            pstmt.executeUpdate();

            Stage stage = (Stage) txtIzena.getScene().getWindow();
            stage.close();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void atzera() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }
}
