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
    @FXML private ComboBox<String> materialaCombo;
    @FXML private Spinner<Integer> kantitateaSpinner;
    @FXML private TextField prezioaField;
    @FXML private Button gordeBtn;
    @FXML private Button utziBtn;

    private final Map<String, Integer> hornitzaileMap = new HashMap<>();
    private final Map<String, Integer> osagaiIdMap = new HashMap<>();
    private final Map<String, Double> osagaiPrezioMap = new HashMap<>();
    private final Map<String, Integer> materialIdMap = new HashMap<>();
    private final Map<String, Double> materialPrezioMap = new HashMap<>();
    private double prezioUnitario = 0.0;

    @FXML
    public void initialize() {
        prezioaField.setEditable(false);
        prezioaField.setStyle("-fx-background-color: #e0e0e0;");
        kantitateaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
        produktuaCombo.setDisable(true);
        materialaCombo.setDisable(true);
        kargatuHornitzaileak();

        hornitzaileCombo.setOnAction(e -> {
            String izena = hornitzaileCombo.getValue();
            if (izena != null) {
                int id = hornitzaileMap.get(izena);
                kargatuOsagaiak(id);
                kargatuMaterialak(id);
            }
        });

        produktuaCombo.setOnAction(e -> eguneratuPrezioa());
        materialaCombo.setOnAction(e -> eguneratuPrezioa());
        kantitateaSpinner.valueProperty().addListener((obs, oldV, newV) -> eguneratuPrezioa());
        gordeBtn.setOnAction(e -> gordeErosketa());
    }

    private void eguneratuPrezioa() {
        String osagaia = produktuaCombo.getValue();
        String materiala = materialaCombo.getValue();

        if (osagaia != null && osagaiPrezioMap.containsKey(osagaia)) {
            prezioUnitario = osagaiPrezioMap.get(osagaia);
        } else if (materiala != null && materialPrezioMap.containsKey(materiala)) {
            prezioUnitario = materialPrezioMap.get(materiala);
        } else {
            prezioaField.clear();
            prezioUnitario = 0;
            return;
        }

        int kantitatea = kantitateaSpinner.getValue();
        prezioaField.setText(String.format("%.2f", prezioUnitario * kantitatea));
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
                boolean found = false;
                while (rs.next()) {
                    String izena = rs.getString("izena");
                    int id = rs.getInt("id");
                    double prezioa = rs.getDouble("prezioa");
                    produktuaCombo.getItems().add(izena);
                    osagaiIdMap.put(izena, id);
                    osagaiPrezioMap.put(izena, prezioa);
                    found = true;
                }
                produktuaCombo.setDisable(!found);
                if (found) produktuaCombo.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void kargatuMaterialak(int hornitzaileaId) {
        materialaCombo.getItems().clear();
        materialIdMap.clear();
        materialPrezioMap.clear();

        String sql = "SELECT id, izena, prezioa FROM materialak WHERE hornitzaileak_id = ?";
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hornitzaileaId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    String izena = rs.getString("izena");
                    int id = rs.getInt("id");
                    double prezioa = rs.getDouble("prezioa");
                    materialaCombo.getItems().add(izena);
                    materialIdMap.put(izena, id);
                    materialPrezioMap.put(izena, prezioa);
                    found = true;
                }
                materialaCombo.setDisable(!found);
                if (found) materialaCombo.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gordeErosketa() {
        String hornitzaileIzena = hornitzaileCombo.getValue();
        String osagaiaIzena = produktuaCombo.getValue();
        String materialaIzena = materialaCombo.getValue();

        if (hornitzaileIzena == null) {
            alerta("Errorea", "Hornitzailea aukeratu behar da");
            return;
        }

        int hornitzaileaId = hornitzaileMap.get(hornitzaileIzena);
        int kantitatea = kantitateaSpinner.getValue();
        double prezioa = prezioUnitario * kantitatea;

        try (Connection conn = Konexioa.getConnection()) {
            conn.setAutoCommit(false);

            if (osagaiaIzena != null && osagaiIdMap.containsKey(osagaiaIzena)) {
                int osagaiaId = osagaiIdMap.get(osagaiaIzena);
                PreparedStatement ps1 = conn.prepareStatement("INSERT INTO erosketa (hornitzailea_id, osagaia_id, kantitatea, prezioa) VALUES (?, ?, ?, ?)");
                ps1.setInt(1, hornitzaileaId);
                ps1.setInt(2, osagaiaId);
                ps1.setInt(3, kantitatea);
                ps1.setDouble(4, prezioa);
                ps1.executeUpdate();

                PreparedStatement ps2 = conn.prepareStatement("UPDATE osagaiak SET stock = stock + ? WHERE id = ?");
                ps2.setInt(1, kantitatea);
                ps2.setInt(2, osagaiaId);
                ps2.executeUpdate();
            }

            if (materialaIzena != null && materialIdMap.containsKey(materialaIzena)) {
                int materialaId = materialIdMap.get(materialaIzena);
                PreparedStatement ps1 = conn.prepareStatement("INSERT INTO erosketa (hornitzailea_id, materiala_id, kantitatea, prezioa) VALUES (?, ?, ?, ?)");
                ps1.setInt(1, hornitzaileaId);
                ps1.setInt(2, materialaId);
                ps1.setInt(3, kantitatea);
                ps1.setDouble(4, prezioa);
                ps1.executeUpdate();

                PreparedStatement ps2 = conn.prepareStatement("UPDATE materialak SET stock = stock + ? WHERE id = ?");
                ps2.setInt(1, kantitatea);
                ps2.setInt(2, materialaId);
                ps2.executeUpdate();
            }

            conn.commit();
            alerta("Ondo", "Erosketa hornitzaileari bidalita");

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
