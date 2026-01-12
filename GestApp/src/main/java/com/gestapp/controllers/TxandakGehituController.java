package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TxandakGehituController {

    @FXML
    private DatePicker txtDate;
    @FXML
    private TextField txtTxandaZenbakia;
    @FXML
    private ComboBox<String> cmbLangilea;

    private Map<String, Integer> langileaMap = new HashMap<>();

    @FXML
    private void initialize() {
        String sql = "SELECT id, izena FROM langileak";
        ObservableList<String> langileak = FXCollections.observableArrayList();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String izena = rs.getString("izena");
                int id = rs.getInt("id");
                langileak.add(izena);
                langileaMap.put(izena, id);
            }

            cmbLangilea.setItems(langileak);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gorde() {
        String sql = "INSERT INTO txandak (date, txanda_zenbakia, langileak_id) VALUES (?, ?, ?)";
        Stage owner = (Stage) cmbLangilea.getScene().getWindow();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            LocalDate localDate = txtDate.getValue();
            String langileaIzena = cmbLangilea.getValue();

            pstmt.setDate(1, Date.valueOf(localDate));
            pstmt.setInt(2, Integer.parseInt(txtTxandaZenbakia.getText()));
            pstmt.setInt(3, langileaMap.get(langileaIzena));

            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ondo");
            ok.setHeaderText(null);
            ok.setContentText("Txanda ondo gorde da");
            ok.showAndWait();

            owner.close();

        } catch (SQLException | NumberFormatException | NullPointerException e) {
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
        ((Stage) cmbLangilea.getScene().getWindow()).close();
    }
}
