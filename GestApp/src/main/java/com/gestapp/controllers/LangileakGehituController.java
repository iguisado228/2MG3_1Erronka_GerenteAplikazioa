package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LangileakGehituController {

    @FXML private TextField txtLangileKodea;
    @FXML private TextField txtIzena;
    @FXML private TextField txtAbizena;
    @FXML private TextField txtNan;
    @FXML private TextField txtErabiltzaileIzena;
    @FXML private TextField txtPasahitza;
    @FXML private CheckBox chkGerentea;
    @FXML private TextField txtHelbidea;
    @FXML private CheckBox chkTpvSarrera;

    @FXML
    private void gorde() {

        String sql = "INSERT INTO langileak (langile_kodea, izena, abizena, nan, erabiltzaile_izena, pasahitza, gerentea, helbidea, tpv_sarrera) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int langileKodea;
            try {
                langileKodea = Integer.parseInt(txtLangileKodea.getText());
            } catch (NumberFormatException e) {
                erakutsiErrorea("Langile kode okerra", "Langile kodeak zenbaki bat izan behar du.");
                return;
            }

            String pasahitzaHashea = hash(txtPasahitza.getText());

            pstmt.setInt(1, langileKodea);
            pstmt.setString(2, txtIzena.getText());
            pstmt.setString(3, txtAbizena.getText());
            pstmt.setString(4, txtNan.getText());
            pstmt.setString(5, txtErabiltzaileIzena.getText());
            pstmt.setString(6, pasahitzaHashea);
            pstmt.setBoolean(7, chkGerentea.isSelected());
            pstmt.setString(8, txtHelbidea.getText());
            pstmt.setBoolean(9, chkTpvSarrera.isSelected());

            pstmt.executeUpdate();

            Stage stage = (Stage) txtIzena.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            erakutsiErrorea("Datu-base errorea", "Ezin izan da langilea gorde. Datuak egiaztatu.");
        }
    }

    private String hash(String value) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = md.digest(value.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private void erakutsiErrorea(String titulua, String mezua) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errorea");
        alert.setHeaderText(titulua);
        alert.setContentText(mezua);
        alert.initOwner(txtIzena.getScene().getWindow());
        alert.show();
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
    }

    @FXML
    private void atzera() {
        Stage stage = (Stage) txtIzena.getScene().getWindow();
        stage.close();
    }
}
