package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Mahaia;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MahaiakEditatuController {

    @FXML
    private TextField txtZenbakia;
    @FXML
    private TextField txtPertsonaKopurua;
    @FXML
    private TextField txtKokapena;

    private Mahaia mahaia;

    public void setMahaia(Mahaia m) {
        this.mahaia = m;
        txtZenbakia.setText(String.valueOf(m.getZenbakia()));
        txtPertsonaKopurua.setText(String.valueOf(m.getPertsonaKopurua()));
        txtKokapena.setText(m.getKokapena());
    }

    @FXML
    private void gorde() {
        String sql = "UPDATE mahaiak SET zenbakia=?, pertsona_kopuru=?, kokapena=? WHERE id=?";

        Stage owner = (Stage) txtZenbakia.getScene().getWindow();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(this.txtZenbakia.getText()));
            pstmt.setInt(2, Integer.parseInt(this.txtPertsonaKopurua.getText()));
            pstmt.setString(3, txtKokapena.getText());
            pstmt.setInt(4, mahaia.getId());

            int rows = pstmt.executeUpdate();

            Alert alert;
            if (rows > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(owner);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Ondo");
                alert.setHeaderText(null);
                alert.setContentText("Mahaia ondo eguneratu da");
                alert.showAndWait();
                owner.close();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(owner);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Errorea");
                alert.setHeaderText(null);
                alert.setContentText("Ezin izan da mahaia eguneratu");
                alert.showAndWait();
            }

        } catch (SQLException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(owner);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Errorea eguneratzean");
            alert.showAndWait();
        }
    }

    @FXML
    private void ezGorde() {
        ((Stage) txtZenbakia.getScene().getWindow()).close();
    }
}
