package com.gestapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.gestapp.hasheadorea.Hasheadorea;
import com.gestapp.konexioa.Konexioa;

import java.io.IOException;
import java.sql.*;

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

            String hash = Hasheadorea.pasahitzaHasheatu(pasahitza);

            String sqlUser = "SELECT gerentea FROM langileak WHERE erabiltzaile_izena=? AND pasahitza=?";
            PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
            stmtUser.setString(1, erabiltzailea);
            stmtUser.setString(2, hash);

            ResultSet rsUser = stmtUser.executeQuery();

            if (!rsUser.next()) {
                alerta(Alert.AlertType.ERROR, "Errorea", "Erabiltzaile edo pasahitz okerra.");
                return;
            }

            boolean gerentea = rsUser.getBoolean("gerentea");

            if (!gerentea) {
                alerta(Alert.AlertType.ERROR, "Sarbidea ukatuta", "Ez duzu sarbiderik. Gerentea izan behar zara.");
                return;
            }

            alerta(Alert.AlertType.INFORMATION, "Login ondo!", "Ongi etorri " + erabiltzailea + "!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Scene menuScene = new Scene(loader.load());

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(menuScene);
            stage.setTitle("Menua");
            stage.setFullScreen(true);
            stage.show();

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
