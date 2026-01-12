package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        Stage owner = (Stage) txtIzena.getScene().getWindow();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtIzena.getText());
            pstmt.setDouble(2, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(3, txtMota.getText());
            pstmt.setInt(4, Integer.parseInt(txtStock.getText()));

            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ongi atera da");
            ok.setHeaderText(null);
            ok.setContentText("Produktua ongi gorde da.");
            ok.showAndWait();

            owner.close();

        } catch (NumberFormatException e) {
            Alert errorea = new Alert(Alert.AlertType.ERROR);
            errorea.initOwner(owner);
            errorea.setTitle("Errorea");
            errorea.setHeaderText(null);
            errorea.setContentText("Datuak falta dira betetzeko.");
            errorea.showAndWait();
        } catch (SQLException e) {
            Alert errorea = new Alert(Alert.AlertType.ERROR);
            errorea.initOwner(owner);
            errorea.setTitle("Errorea");
            errorea.setHeaderText(null);
            errorea.setContentText("Errorea gertatu da produktua gordetzean.");
            errorea.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void atzera() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }
}
