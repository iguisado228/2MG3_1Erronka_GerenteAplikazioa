package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Eskaria;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EskariakEditatuController {

    @FXML private TextField txtPrezioa;
    @FXML private TextField txtEgoera;
    @FXML private ComboBox<Integer> cbErreserbak;
    @FXML private ComboBox<String> cbLangileak;
    @FXML private ComboBox<String> cbMahaiak;

    private Eskaria eskaria;

    public void setEskaria(Eskaria e) {
        this.eskaria = e;
        txtPrezioa.setText(String.valueOf(e.getPrezioa()));
        txtEgoera.setText(e.getEgoera());
        cbErreserbak.setValue(e.getErreserbak_id());
        cbLangileak.setValue(e.getLangileIzena());
        cbMahaiak.setValue(e.getMahaiakKokapena());
    }

    @FXML
    private void gorde() {
        Stage owner = (Stage) txtPrezioa.getScene().getWindow();
        String sql = "UPDATE eskariak SET prezioa=?, egoera=?, erreserbak_id=?, erreserbak_langileak_id=?, erreserbak_mahaiak_id=? WHERE id=?";
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(2, txtEgoera.getText());
            pstmt.setInt(3, cbErreserbak.getValue());
            pstmt.setInt(4, cbLangileak.getSelectionModel().getSelectedIndex() + 1);
            pstmt.setInt(5, cbMahaiak.getSelectionModel().getSelectedIndex() + 1);
            pstmt.setInt(6, eskaria.getId());
            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ondo");
            ok.setHeaderText(null);
            ok.setContentText("Eskaria ongi eguneratu da.");
            ok.showAndWait();
            owner.close();

        } catch (Exception e) {
            Alert errorea = new Alert(Alert.AlertType.ERROR);
            errorea.initOwner(owner);
            errorea.setTitle("Errorea");
            errorea.setHeaderText(null);
            errorea.setContentText("Errorea gertatu da eguneratzean.");
            errorea.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void ezGorde() { ((Stage) txtPrezioa.getScene().getWindow()).close(); }
}
