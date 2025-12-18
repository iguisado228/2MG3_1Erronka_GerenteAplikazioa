package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Langilea;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LangileaEditatuController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtAbizena;
    @FXML private TextField txtNan;
    @FXML private TextField txtErabiltzaileIzena;
    @FXML private TextField txtPasahitza;
    @FXML private CheckBox chkGerentea;
    @FXML private TextField txtHelbidea;
    @FXML private TextField txtLangileKodea;
    @FXML private CheckBox chkTpvSarrera;

    private Langilea langilea;

    public void setLangilea(Langilea l) {
        this.langilea = l;
        txtIzena.setText(l.getIzena());
        txtAbizena.setText(l.getAbizena());
        txtNan.setText(l.getNan());
        txtErabiltzaileIzena.setText(l.getErabiltzaileIzena());
        txtPasahitza.setText(l.getPasahitza());
        chkGerentea.setSelected(l.isGerentea());
        txtHelbidea.setText(l.getHelbidea());
        txtLangileKodea.setText(String.valueOf(l.getLangileKodea()));
        chkTpvSarrera.setSelected(l.isTpvSarrera());
    }

    @FXML
    private void gorde() {

        String sql = """ 
            UPDATE langileak SET 
                izena=?, 
                abizena=?, 
                nan=?, 
                erabiltzaile_izena=?, 
                pasahitza=?, 
                gerentea=?, 
                helbidea=?, 
                langile_kodea=?, 
                tpv_sarrera=? 
            WHERE id=? 
        """;

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int langileKodea;
            try {
                langileKodea = Integer.parseInt(txtLangileKodea.getText());
            } catch (NumberFormatException e) {
                erakutsiErrorea("Langile kode okerra", "Langile kodeak zenbaki bat izan behar du.");
                return;
            }

            pstmt.setString(1, txtIzena.getText());
            pstmt.setString(2, txtAbizena.getText());
            pstmt.setString(3, txtNan.getText());
            pstmt.setString(4, txtErabiltzaileIzena.getText());
            pstmt.setString(5, txtPasahitza.getText());
            pstmt.setBoolean(6, chkGerentea.isSelected());
            pstmt.setString(7, txtHelbidea.getText());
            pstmt.setInt(8, langileKodea);
            pstmt.setBoolean(9, chkTpvSarrera.isSelected());
            pstmt.setInt(10, langilea.getId());

            pstmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Editatu");
            alert.setHeaderText(null);
            alert.setContentText("Langilea eguneratu da arrakastaz.");
            alert.initOwner(txtIzena.getScene().getWindow());
            alert.showAndWait();

            Stage stage = (Stage) txtIzena.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            erakutsiErrorea("Datu-base errorea", "Ezin izan da langilea eguneratu.");
        }
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
    private void ezGorde() {
        Stage stage = (Stage) txtIzena.getScene().getWindow();
        stage.close();
    }
}