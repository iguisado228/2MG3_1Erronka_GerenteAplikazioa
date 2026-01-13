package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OsagaiakGehituController {

    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtPrezioa;
    @FXML
    private TextField txtStock;
    @FXML
    private ComboBox<HornitzaileaItem> cmbHornitzailea;

    private ObservableList<HornitzaileaItem> hornitzaileakList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, izena FROM hornitzaileak");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                hornitzaileakList.add(new HornitzaileaItem(rs.getInt("id"), rs.getString("izena")));
            }
            cmbHornitzailea.setItems(hornitzaileakList);
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Errorea hornitzaileak kargatzean");
            alerta.showAndWait();
        }
    }

    @FXML
    private void gorde() {
        if (cmbHornitzailea.getValue() == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Aukeratu hornitzailea");
            alerta.showAndWait();
            return;
        }
        String sql = "INSERT INTO osagaiak (izena, prezioa, stock, hornitzaileak_id) VALUES (?, ?, ?, ?)";
        Stage owner = (Stage) txtIzena.getScene().getWindow();
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, txtIzena.getText());
            pstmt.setDouble(2, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setInt(3, Integer.parseInt(txtStock.getText()));
            pstmt.setInt(4, cmbHornitzailea.getValue().getId());
            pstmt.executeUpdate();
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setHeaderText(null);
            ok.setTitle("Ondo");
            ok.setContentText("Osagaia ondo gorde da");
            ok.showAndWait();
            owner.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(owner);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Errorea osagaia gordetzean");
            alerta.showAndWait();
        }
    }

    @FXML
    private void atzera() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }

    public static class HornitzaileaItem {
        private final int id;
        private final String izena;

        public HornitzaileaItem(int id, String izena) {
            this.id = id;
            this.izena = izena;
        }
        public int getId() { return id; }
        @Override
        public String toString() { return izena; }
    }
}
