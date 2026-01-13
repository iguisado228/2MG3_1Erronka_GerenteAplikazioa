package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EskariakGehituController {

    @FXML private TextField txtPrezioa;
    @FXML private TextField txtEgoera;
    @FXML private ComboBox<Integer> cbErreserbak;
    @FXML private ComboBox<String> cbLangileak;
    @FXML private ComboBox<String> cbMahaiak;

    @FXML
    private void initialize() {
        try (Connection conn = Konexioa.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT id FROM erreserbak");
            while(rs.next()) cbErreserbak.getItems().add(rs.getInt("id"));
            rs = conn.createStatement().executeQuery("SELECT izena FROM erreserbak_langileak");
            while(rs.next()) cbLangileak.getItems().add(rs.getString("izena"));
            rs = conn.createStatement().executeQuery("SELECT kokapena FROM erreserbak_mahaiak");
            while(rs.next()) cbMahaiak.getItems().add(rs.getString("kokapena"));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void gorde() {
        Stage owner = (Stage) txtPrezioa.getScene().getWindow();
        String sql = "INSERT INTO eskariak (prezioa, egoera, erreserbak_id, erreserbak_langileak_id, erreserbak_mahaiak_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(2, txtEgoera.getText());
            pstmt.setInt(3, cbErreserbak.getValue());
            pstmt.setInt(4, cbLangileak.getSelectionModel().getSelectedIndex() + 1);
            pstmt.setInt(5, cbMahaiak.getSelectionModel().getSelectedIndex() + 1);
            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ongi atera da");
            ok.setHeaderText(null);
            ok.setContentText("Eskaria ongi gorde da.");
            ok.showAndWait();
            owner.close();

        } catch (Exception e) {
            Alert errorea = new Alert(Alert.AlertType.ERROR);
            errorea.initOwner(owner);
            errorea.setTitle("Errorea");
            errorea.setHeaderText(null);
            errorea.setContentText("Errorea gertatu da eskaria gordetzean.");
            errorea.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void atzera() { ((Stage) txtPrezioa.getScene().getWindow()).close(); }
}
