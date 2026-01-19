package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialakGehituController {

    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtPrezioa;
    @FXML
    private TextField txtStock;
    @FXML
    private ComboBox<Hornitzaileak> cmbHornitzailea;

    private ObservableList<Hornitzaileak> hornitzaileakZerrenda = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        txtIzena.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                hornitzaileakKargatu();
            }
        });
    }

    private void hornitzaileakKargatu() {
        Stage jabe = (Stage) txtIzena.getScene().getWindow();
        try (Connection konexioa = Konexioa.getConnection();
             PreparedStatement stmt = konexioa.prepareStatement("SELECT id, izena FROM hornitzaileak");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                hornitzaileakZerrenda.add(new Hornitzaileak(
                        rs.getInt("id"),
                        rs.getString("izena")
                ));
            }
            cmbHornitzailea.setItems(hornitzaileakZerrenda);

        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Errorea hornitzaileak kargatzean");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void gorde() {
        Stage jabe = (Stage) txtIzena.getScene().getWindow();

        if (cmbHornitzailea.getValue() == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Aukeratu hornitzailea");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
            return;
        }

        String sql = "INSERT INTO materialak (izena, prezioa, stock, hornitzaileak_id) VALUES (?, ?, ?, ?)";

        try (Connection konexioa = Konexioa.getConnection();
             PreparedStatement pstmt = konexioa.prepareStatement(sql)) {

            pstmt.setString(1, txtIzena.getText());
            pstmt.setDouble(2, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setInt(3, Integer.parseInt(txtStock.getText()));
            pstmt.setInt(4, cmbHornitzailea.getValue().getId());
            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION, "Materiala ondo gorde da");
            ok.initOwner(jabe);
            ok.initModality(Modality.APPLICATION_MODAL);
            ok.showAndWait();
            jabe.close();

        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Errorea materiala gordetzean");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Mesedez, prezioa eta stock zenbakiak izan behar dira");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void atzera() {
        Stage jabe = (Stage) txtIzena.getScene().getWindow();
        jabe.close();
    }

    public static class Hornitzaileak {
        private final int id;
        private final String izena;

        public Hornitzaileak(int id, String izena) {
            this.id = id;
            this.izena = izena;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return izena;
        }
    }
}
