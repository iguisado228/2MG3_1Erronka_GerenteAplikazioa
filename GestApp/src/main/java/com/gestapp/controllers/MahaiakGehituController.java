package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MahaiakGehituController {

    @FXML
    private TextField txtZenbakia;
    @FXML
    private TextField txtPertsonaKopurua;
    @FXML
    private TextField txtKokapena;

    @FXML
    private void gorde() {
        String sql = "INSERT INTO mahaiak (zenbakia, pertsona_kopuru, kokapena) VALUES (?, ?, ?)";
        Stage owner = (Stage) txtZenbakia.getScene().getWindow();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(txtZenbakia.getText()));
            pstmt.setInt(2, Integer.parseInt(txtPertsonaKopurua.getText()));
            pstmt.setString(3, txtKokapena.getText());

            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ondo");
            ok.setHeaderText(null);
            ok.setContentText("Mahaia ondo gorde da");
            ok.showAndWait();

            owner.close();

        } catch (SQLException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(owner);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Errorea gordetzean");
            alert.showAndWait();
        }
    }

    @FXML
    private void atzera() {
        ((Stage) txtZenbakia.getScene().getWindow()).close();
    }
}
