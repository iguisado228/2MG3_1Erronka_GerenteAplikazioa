package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Osagaia;
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

public class OsagaiakEditatuController {

    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtPrezioa;
    @FXML
    private TextField txtStock;
    @FXML
    private ComboBox<OsagaiakGehituController.HornitzaileaItem> cmbHornitzailea;

    private Osagaia osagaia;
    private ObservableList<OsagaiakGehituController.HornitzaileaItem> hornitzaileakList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, izena FROM hornitzaileak");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                hornitzaileakList.add(new OsagaiakGehituController.HornitzaileaItem(rs.getInt("id"), rs.getString("izena")));
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

    public void setOsagaia(Osagaia o) {
        this.osagaia = o;
        txtIzena.setText(o.getIzena());
        txtPrezioa.setText(String.valueOf(o.getPrezioa()));
        txtStock.setText(String.valueOf(o.getStock()));
        for (OsagaiakGehituController.HornitzaileaItem h : hornitzaileakList) {
            if (h.getId() == o.getHornitzaileaId()) {
                cmbHornitzailea.setValue(h);
                break;
            }
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
        String sql = "UPDATE osagaiak SET izena=?, prezioa=?, stock=?, hornitzaileak_id=? WHERE id=?";
        Stage owner = (Stage) txtIzena.getScene().getWindow();
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, txtIzena.getText());
            pstmt.setDouble(2, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setInt(3, Integer.parseInt(txtStock.getText()));
            pstmt.setInt(4, cmbHornitzailea.getValue().getId());
            pstmt.setInt(5, osagaia.getId());
            int rows = pstmt.executeUpdate();
            Alert alerta;
            if (rows > 0) {
                alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.initOwner(owner);
                alerta.initModality(Modality.APPLICATION_MODAL);
                alerta.setTitle("Ondo");
                alerta.setHeaderText(null);
                alerta.setContentText("Osagaia ondo eguneratu da");
                alerta.showAndWait();
                owner.close();
            } else {
                alerta = new Alert(Alert.AlertType.ERROR);
                alerta.initOwner(owner);
                alerta.initModality(Modality.APPLICATION_MODAL);
                alerta.setTitle("Errorea");
                alerta.setHeaderText(null);
                alerta.setContentText("Ezin izan da osagaia eguneratu");
                alerta.showAndWait();
            }
        } catch (SQLException e) {
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
