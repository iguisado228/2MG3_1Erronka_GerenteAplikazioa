package com.gestapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

import com.gestapp.hasheadorea.Hasheadorea;

import javafx.stage.Stage;
import com.gestapp.konexioa.Konexioa;


public class LoginController {

    @FXML
    private TextField tfErabiltzailea;

    @FXML
    private TextField tfPasahitza;

    @FXML
    private Button btnLogin;


    private void alerta(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void login() {
        String erabiltzailea = tfErabiltzailea.getText();
        String pasahitza = tfPasahitza.getText();

        if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            alerta(Alert.AlertType.WARNING, "Errorea", "Mesedez, bete bi eremuak.");
            return;
        }

        try (Connection conn = Konexioa.getConnection()) {


            String hasheadorea = Hasheadorea.pasahitzaHasheatu(pasahitza);


            String sql = "SELECT id FROM langileak WHERE erabiltzaile_izena=? AND pasahitza=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, erabiltzailea);
            stmt.setString(2, hasheadorea);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                alerta(Alert.AlertType.INFORMATION, "Login ondo!", "Ongi etorri " + erabiltzailea + "!");




    FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/gestapp/main/menu-view.fxml")
    );
    Scene menuScene = new Scene(loader.load());


    Stage stage = (Stage) btnLogin.getScene().getWindow();

    stage.setScene(menuScene);
    stage.setTitle("Menua");
    stage.setFullScreen(true);
    stage.show();


            } else {
                alerta(Alert.AlertType.ERROR, "Errorea", "Erabiltzaile edo pasahitz okerra.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            alerta(Alert.AlertType.ERROR, "Errore teknikoa", "Datu-basearekin arazoa egon da.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onbtnLoginClick(ActionEvent actionEvent) {
        login();
    }

    @FXML
    public void onbtnAteraClick(ActionEvent actionEvent) {
        System.exit(0);
    }
}
