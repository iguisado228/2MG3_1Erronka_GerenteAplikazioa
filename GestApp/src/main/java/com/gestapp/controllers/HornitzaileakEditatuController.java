package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Hornitzailea;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HornitzaileakEditatuController {

    @FXML
    private TextField txtIzena;

    @FXML
    private TextField txtKontaktua;

    @FXML
    private TextField txtHelbidea;

    private Hornitzailea hornitzailea;

    public void setHornitzailea(Hornitzailea h) {
        this.hornitzailea= h;
        txtIzena.setText(h.getIzena());
        txtKontaktua.setText(h.getKontaktua());
        txtHelbidea.setText(h.getHelbidea());

    }

    @FXML
    private void gorde() {
        String sql = "UPDATE hornitzaileak SET izena=?, kontaktua=?, helbidea=? WHERE id=?";

        Stage owner = (Stage) txtIzena.getScene().getWindow();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString (1, txtIzena.getText());
            pstmt.setString(2, txtKontaktua.getText());
            pstmt.setString(3, txtHelbidea.getText());
            pstmt.setInt(4, hornitzailea.getId());

            int rows = pstmt.executeUpdate();

            Alert alerta;

            if (rows > 0) {
                alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.initOwner(owner);
                alerta.initModality(Modality.APPLICATION_MODAL);
                alerta.setTitle("Ondo");
                alerta.setHeaderText(null);
                alerta.setContentText("Hornitzailea ondo eguneratu da");
                alerta.showAndWait();
                owner.close();
            }else {
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.initOwner(owner);
                alerta.initModality(Modality.APPLICATION_MODAL);
                alerta.setTitle("Errorea");
                alerta.setHeaderText(null);
                alerta.setContentText("Ezin izan da hornitzailea eguneratu");
            }
        }catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.initOwner(owner);
            a.initModality(Modality.APPLICATION_MODAL);
            a.setTitle("Errore");
            a.setHeaderText(null);
            a.setContentText("Errorea egon da datuak eguneratzean");
            a.showAndWait();
        }
    }

    @FXML
    private void ezGorde() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }
}
