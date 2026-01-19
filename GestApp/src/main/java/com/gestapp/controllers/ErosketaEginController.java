package com.gestapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.gestapp.konexioa.Konexioa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ErosketaEginController {

    @FXML private ComboBox<String> hornitzaileCombo;
    @FXML private ComboBox<String> produktuaCombo;
    @FXML private Spinner<Integer> kantitateaSpinner;
    @FXML private TextField prezioaField;
    @FXML private Button gordeBtn;
    @FXML private Button utziBtn;

    private final Map<String, Integer> hornitzaileMap = new HashMap<>();
    private final Map<String, Integer> osagaiIdMap = new HashMap<>();
    private final Map<String, Double> osagaiPrezioMap = new HashMap<>();
    private double prezioUnitario = 0.0;

    @FXML
    public void initialize() {
        prezioaField.setEditable(false);
        prezioaField.setStyle("-fx-background-color: #e0e0e0;");
        kantitateaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        produktuaCombo.setDisable(true);
        kargatuHornitzaileak();

        hornitzaileCombo.setOnAction(e -> {
            String hornitzaileIzena = hornitzaileCombo.getValue();
            if (hornitzaileIzena != null) {
                int hornitzaileaId = hornitzaileMap.get(hornitzaileIzena);
                kargatuOsagaiak(hornitzaileaId);
            }
        });

        produktuaCombo.setOnAction(e -> eguneratuPrezioa());
        kantitateaSpinner.valueProperty().addListener((obs, oldV, newV) -> eguneratuPrezioa());
        gordeBtn.setOnAction(e -> gordeErosketa());
    }

    private void eguneratuPrezioa() {
        String izena = produktuaCombo.getValue();
        if (izena != null && osagaiPrezioMap.containsKey(izena)) {
            prezioUnitario = osagaiPrezioMap.get(izena);
            int kantitatea = kantitateaSpinner.getValue();
            prezioaField.setText(String.format("%.2f", prezioUnitario * kantitatea));
        } else {
            prezioaField.clear();
            prezioUnitario = 0;
        }
    }

    private void kargatuHornitzaileak() {
        String sql = "SELECT id, izena FROM hornitzaileak";
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String izena = rs.getString("izena");
                int id = rs.getInt("id");
                hornitzaileCombo.getItems().add(izena);
                hornitzaileMap.put(izena, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void kargatuOsagaiak(int hornitzaileaId) {
        produktuaCombo.getItems().clear();
        osagaiIdMap.clear();
        osagaiPrezioMap.clear();

        String sql = "SELECT id, izena, prezioa FROM osagaiak WHERE hornitzaileak_id = ?";
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hornitzaileaId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hasProducts = false;
                while (rs.next()) {
                    String izena = rs.getString("izena");
                    int id = rs.getInt("id");
                    double prezioa = rs.getDouble("prezioa");
                    produktuaCombo.getItems().add(izena);
                    osagaiIdMap.put(izena, id);
                    osagaiPrezioMap.put(izena, prezioa);
                    hasProducts = true;
                }
                produktuaCombo.setDisable(!hasProducts);
                if (hasProducts) {
                    produktuaCombo.getSelectionModel().selectFirst();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gordeErosketa() {
        String hornitzaileIzena = hornitzaileCombo.getValue();
        String osagaiaIzena = produktuaCombo.getValue();
        if (hornitzaileIzena == null || osagaiaIzena == null || !osagaiIdMap.containsKey(osagaiaIzena)) {
            alerta("Errorea", "Datu guztiak bete behar dira");
            return;
        }
        int kantitatea = kantitateaSpinner.getValue();
        int hornitzaileaId = hornitzaileMap.get(hornitzaileIzena);
        int osagaiaId = osagaiIdMap.get(osagaiaIzena);
        double prezioa = prezioUnitario * kantitatea;

        String insertSql = "INSERT INTO erosketa (hornitzailea_id, osagaia_id, kantitatea, prezioa) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE osagaiak SET stock = stock + ? WHERE id = ?";

        try (Connection conn = Konexioa.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql);
                 PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {

                psInsert.setInt(1, hornitzaileaId);
                psInsert.setInt(2, osagaiaId);
                psInsert.setInt(3, kantitatea);
                psInsert.setDouble(4, prezioa);
                psInsert.executeUpdate();

                psUpdate.setInt(1, kantitatea);
                psUpdate.setInt(2, osagaiaId);
                psUpdate.executeUpdate();

                conn.commit();
                alerta("Ondo", "Erosketa ondo gorde da");

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea", "Errorea erosketa gordetzean");
        }
    }



    private void alerta(String titulua, String mezua) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulua);
        alert.setHeaderText(null);
        alert.setContentText(mezua);
        alert.initOwner(gordeBtn.getScene().getWindow());
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
    }
}
