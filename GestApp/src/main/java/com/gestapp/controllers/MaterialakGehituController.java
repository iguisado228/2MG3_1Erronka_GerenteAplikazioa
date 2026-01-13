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

    private ObservableList<Hornitzaileak> hornitzaileakList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        Stage owner = (Stage) txtIzena.getScene().getWindow();
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, izena FROM hornitzaileak");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                hornitzaileakList.add(new Hornitzaileak(
                        rs.getInt("id"),
                        rs.getString("izena")
                ));
            }
            cmbHornitzailea.setItems(hornitzaileakList);
        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Errorea hornitzaileak kargatzean");
            a.initOwner(owner);
            a.initModality(Modality.APPLICATION_MODAL);
            a.showAndWait();
        }
    }

    @FXML
    private void gorde() {
        Stage owner = (Stage) txtIzena.getScene().getWindow();

        if (cmbHornitzailea.getValue() == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Aukeratu hornitzailea");
            a.initOwner(owner);
            a.initModality(Modality.APPLICATION_MODAL);
            a.showAndWait();
            return;
        }

        String sql = "INSERT INTO materialak (izena, prezioa, stock, hornitzaileak_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtIzena.getText());
            pstmt.setDouble(2, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setInt(3, Integer.parseInt(txtStock.getText()));
            pstmt.setInt(4, cmbHornitzailea.getValue().getId());
            pstmt.executeUpdate();

            Alert ok = new Alert(Alert.AlertType.INFORMATION, "Materiala ondo gorde da");
            ok.initOwner(owner);
            ok.initModality(Modality.APPLICATION_MODAL);
            ok.showAndWait();
            owner.close();

        } catch (SQLException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Errorea materiala gordetzean");
            a.initOwner(owner);
            a.initModality(Modality.APPLICATION_MODAL);
            a.showAndWait();
        }
    }

    @FXML
    private void atzera() {
        ((Stage) txtIzena.getScene().getWindow()).close();
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
