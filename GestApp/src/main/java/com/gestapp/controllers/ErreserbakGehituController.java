package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ErreserbakGehituController {

    @FXML private TextField txtBezeroIzena;
    @FXML private TextField txtTelefonoa;
    @FXML private TextField txtPertsonak;
    @FXML private DatePicker dpEguna;
    @FXML private TextField txtOrdua;
    @FXML private TextField txtPrezioa;
    @FXML private TextField txtFaktura;
    @FXML private TextField txtLangileId;
    @FXML private TextField txtMahaiId;

    @FXML
    private void gorde() {
        String sql = "INSERT INTO erreserbak (bezero_izena, telefonoa, pertsona_kopurua, eguna, ordua, prezio_totala, faktura_ruta, langileak_id, mahaiak_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtBezeroIzena.getText());
            pstmt.setString(2, txtTelefonoa.getText());
            pstmt.setInt(3, Integer.parseInt(txtPertsonak.getText()));
            pstmt.setDate(4, java.sql.Date.valueOf(dpEguna.getValue()));
            pstmt.setTime(5, java.sql.Time.valueOf(txtOrdua.getText()));
            pstmt.setDouble(6, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(7, txtFaktura.getText());
            pstmt.setInt(8, Integer.parseInt(txtLangileId.getText()));
            pstmt.setInt(9, Integer.parseInt(txtMahaiId.getText()));

            pstmt.executeUpdate();
            ((Stage) txtBezeroIzena.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void atzera() {
        ((Stage) txtBezeroIzena.getScene().getWindow()).close();
    }
}
