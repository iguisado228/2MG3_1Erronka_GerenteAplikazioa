package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Produktua;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProduktuakEditatuController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtPrezioa;
    @FXML private TextField txtMota;
    @FXML private TextField txtStock;

    private Produktua produktua;

    public void setProduktua(Produktua p) {
        this.produktua = p;
        txtIzena.setText(p.getIzena());
        txtPrezioa.setText(String.valueOf(p.getPrezioa()));
        txtMota.setText(p.getMota());
        txtStock.setText(String.valueOf(p.getStock()));
    }

    @FXML
    private void gorde() {
        String sql = "UPDATE produktuak SET izena=?, prezioa=?, mota=?, stock=? WHERE id=?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtIzena.getText());
            pstmt.setDouble(2, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(3, txtMota.getText());
            pstmt.setInt(4, Integer.parseInt(txtStock.getText()));
            pstmt.setInt(5, produktua.getId());

            int rows = pstmt.executeUpdate();

            Alert alert;
            if (rows > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ondo");
                alert.setHeaderText(null);
                alert.setContentText("Produktua ondo eguneratu da");
                alert.showAndWait();
                ((Stage) txtIzena.getScene().getWindow()).close();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errorea");
                alert.setHeaderText(null);
                alert.setContentText("Ezin izan da produktua eguneratu");
                alert.showAndWait();
            }

        } catch (SQLException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Errorea eguneratzean");
            alert.showAndWait();
        }
    }

    @FXML
    private void ezGorde() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }
}
