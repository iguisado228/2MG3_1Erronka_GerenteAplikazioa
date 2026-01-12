package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Txanda;
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
import java.util.HashMap;
import java.util.Map;

public class TxandakEditatuController {

    @FXML
    private DatePicker txtDate;
    @FXML
    private TextField txtTxandaZenbakia;
    @FXML
    private ComboBox<String> cmbLangilea;

    private int txandaId;
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

    public void setTxanda(Txanda t) {
        this.txandaId = t.getId();
        txtDate.setValue(t.getDate());
        txtTxandaZenbakia.setText(String.valueOf(t.getTxandaZenbakia()));
        kargatuLangilea(t.getLangileaId());
    }

    private void kargatuLangilea(int langileaId) {
        String sql = "SELECT izena FROM langileak WHERE id = ?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, langileaId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                cmbLangilea.setValue(rs.getString("izena"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gorde() {
        if (txtDate.getValue() == null || cmbLangilea.getValue() == null || txtTxandaZenbakia.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Kontuz");
            alert.setHeaderText(null);
            alert.setContentText("Eremu guztiak bete behar dira");
            alert.showAndWait();
            return;
        }

        String sql = "UPDATE txandak SET date = ?, txanda_zenbakia = ?, langileak_id = ? WHERE id = ?";
        Stage owner = (Stage) cmbLangilea.getScene().getWindow();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(txtDate.getValue()));
            pstmt.setInt(2, Integer.parseInt(txtTxandaZenbakia.getText()));
            pstmt.setInt(3, langileaMap.get(cmbLangilea.getValue()));
            pstmt.setInt(4, txandaId);

            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ondo");
            ok.setHeaderText(null);
            ok.setContentText("Txanda ondo eguneratu da");
            ok.showAndWait();

            owner.close();

        } catch (SQLException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(owner);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Errorea eguneratzean");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void atzera() {
        ((Stage) cmbLangilea.getScene().getWindow()).close();
    }
}
