package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class ErreserbakGehituController {

    @FXML private TextField txtBezeroIzena;
    @FXML private TextField txtTelefonoa;
    @FXML private TextField txtPertsonak;
    @FXML private DatePicker dpEguna;
    @FXML private TextField txtOrdua;
    @FXML private TextField txtPrezioa;
    @FXML private TextField txtFaktura;
    @FXML private ComboBox<String> cmbLangilea;
    @FXML private ComboBox<String> cmbMahai;

    private Map<String, Integer> langileMap = new HashMap<>();
    private Map<String, Integer> mahaiMap = new HashMap<>();

    @FXML
    private void initialize() {
        kargatuLangileak();
        kargatuMahaiak();
    }

    private void kargatuLangileak() {
        String sql = "SELECT id, izena FROM langileak";
        ObservableList<String> langileak = FXCollections.observableArrayList();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String izena = rs.getString("izena");
                langileak.add(izena);
                langileMap.put(izena, rs.getInt("id"));
            }

            cmbLangilea.setItems(langileak);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void kargatuMahaiak() {
        String sql = "SELECT id, kokapena FROM mahaiak";
        ObservableList<String> mahaiak = FXCollections.observableArrayList();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String kokapena = rs.getString("kokapena");
                mahaiak.add(kokapena);
                mahaiMap.put(kokapena, rs.getInt("id"));
            }

            cmbMahai.setItems(mahaiak);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gorde() {

        if (dpEguna.getValue() == null || cmbLangilea.getValue() == null || cmbMahai.getValue() == null) {
            alerta("Errorea", "Datu falta", "Eremu guztiak bete behar dira");
            return;
        }

        String sql = """
            INSERT INTO erreserbak 
            (bezero_izena, telefonoa, pertsona_kopurua, eguna_ordua, prezio_totala, faktura_ruta, langileak_id, mahaiak_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime ordua;

            try {
                ordua = LocalTime.parse(txtOrdua.getText(), formatter);
            } catch (DateTimeParseException e) {
                alerta("Errorea", "Ordua formatu okerra", "HH:mm erabil ezazu");
                return;
            }

            LocalDateTime dt = LocalDateTime.of(dpEguna.getValue(), ordua);

            pstmt.setString(1, txtBezeroIzena.getText());
            pstmt.setString(2, txtTelefonoa.getText());
            pstmt.setInt(3, Integer.parseInt(txtPertsonak.getText()));
            pstmt.setTimestamp(4, Timestamp.valueOf(dt));
            pstmt.setDouble(5, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(6, txtFaktura.getText());
            pstmt.setInt(7, langileMap.get(cmbLangilea.getValue()));
            pstmt.setInt(8, mahaiMap.get(cmbMahai.getValue()));

            pstmt.executeUpdate();
            arrakasta("Ondo", "Erreserba gorde da", "Erreserba berri bat gehitu da");
            ((Stage) txtBezeroIzena.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            alerta("Errorea", "Zenbaki okerra", "Eremu numerikoak zuzendu");
        } catch (SQLException e) {
            alerta("Errorea", "DB errorea", "Ezin izan da erreserba gorde");
        }
    }

    @FXML
    private void atzera() {
        ((Stage) txtBezeroIzena.getScene().getWindow()).close();
    }

    private void alerta(String titulua, String header, String mezua) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulua);
        alert.setHeaderText(header);
        alert.setContentText(mezua);
        alert.initOwner((Stage) txtBezeroIzena.getScene().getWindow());
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
    }

    private void arrakasta(String titulua, String header, String mezua) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulua);
        alert.setHeaderText(header);
        alert.setContentText(mezua);
        alert.initOwner((Stage) txtBezeroIzena.getScene().getWindow());
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
    }
}
