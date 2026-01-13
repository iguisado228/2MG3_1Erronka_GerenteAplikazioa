package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Materiala;
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

public class MaterialakEditatuController {

    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtPrezioa;
    @FXML
    private TextField txtStock;
    @FXML
    private ComboBox<MaterialakGehituController.Hornitzaileak> cmbHornitzailea;

    private Materiala materiala;
    private ObservableList<MaterialakGehituController.Hornitzaileak> hornitzaileakList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id, izena FROM hornitzaileak");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                hornitzaileakList.add(new MaterialakGehituController.Hornitzaileak(
                        rs.getInt("id"),
                        rs.getString("izena")
                ));
            }
            cmbHornitzailea.setItems(hornitzaileakList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Errorea hornitzaileak kargatzean").showAndWait();
        }
    }

    public void setMateriala(Materiala m) {
        this.materiala = m;
        txtIzena.setText(m.getIzena());
        txtPrezioa.setText(String.valueOf(m.getPrezioa()));
        txtStock.setText(String.valueOf(m.getStock()));

        for (MaterialakGehituController.Hornitzaileak h : hornitzaileakList) {
            if (h.getId() == m.getHornitzaileaId()) {
                cmbHornitzailea.setValue(h);
                break;
            }
        }
    }

    @FXML
    private void gorde() {
        if (cmbHornitzailea.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Aukeratu hornitzailea").showAndWait();
            return;
        }

        String sql = "UPDATE materialak SET izena=?, prezioa=?, stock=?, hornitzaileak_id=? WHERE id=?";
        Stage owner = (Stage) txtIzena.getScene().getWindow();

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtIzena.getText());
            pstmt.setDouble(2, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setInt(3, Integer.parseInt(txtStock.getText()));
            pstmt.setInt(4, cmbHornitzailea.getValue().getId());
            pstmt.setInt(5, materiala.getId());
            pstmt.executeUpdate();

            new Alert(Alert.AlertType.INFORMATION, "Materiala ondo eguneratu da").showAndWait();
            owner.close();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Errorea eguneratzean").showAndWait();
        }
    }

    @FXML
    private void ezGorde() {
        ((Stage) txtIzena.getScene().getWindow()).close();
    }
}
