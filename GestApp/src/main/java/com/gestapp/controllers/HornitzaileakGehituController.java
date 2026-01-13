package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HornitzaileakGehituController {

    @FXML
    private TextField txtIzena;

    @FXML
    private TextField txtKontaktua;
    @FXML
    private TextField txtHelbidea;

    @FXML
    private void gorde(){
        String sql = "INSERT INTO hornitzaileak (izena, kontaktua, helbidea) VALUES (?, ?, ?)";
        Stage owner = (Stage) txtIzena.getScene().getWindow();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, txtIzena.getText());
            pstmt.setString(2, txtKontaktua.getText());
            pstmt.setString(3, txtHelbidea.getText());

            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setHeaderText(null);
            ok.setTitle("Ondo");
            ok.setContentText("Hornitzailea ondo gorde da");
            ok.showAndWait();

            owner.close();


        }catch (SQLException e){
            Alert alerta = new  Alert(Alert.AlertType.ERROR);
            alerta.initOwner(owner);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Errorea hornitzailea gordetzean");
            alerta.showAndWait();
        }
    }

    @FXML
    private void atzera(){
        ((Stage) txtIzena.getScene().getWindow()).close();
    }
}
