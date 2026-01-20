package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Erreserba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class ErreserbakController {

    @FXML private TableView<Erreserba> erreserbak;
    @FXML private TableColumn<Erreserba, Integer> id;
    @FXML private TableColumn<Erreserba, String> bezeroIzena;
    @FXML private TableColumn<Erreserba, String> telefonoa;
    @FXML private TableColumn<Erreserba, Integer> pertsonak;
    @FXML private TableColumn<Erreserba, LocalDateTime> egunaOrdua;
    @FXML private TableColumn<Erreserba, Double> prezioTotala;
    @FXML private TableColumn<Erreserba, String> fakturaRuta;
    @FXML private TableColumn<Erreserba, Integer> langileakId;
    @FXML private TableColumn<Erreserba, Integer> mahaiakId;
    @FXML private TableColumn<Erreserba, Boolean> ordainduta;

    private ObservableList<Erreserba> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        bezeroIzena.setCellValueFactory(new PropertyValueFactory<>("bezeroIzena"));
        telefonoa.setCellValueFactory(new PropertyValueFactory<>("telefonoa"));
        pertsonak.setCellValueFactory(new PropertyValueFactory<>("pertsonaKopurua"));
        egunaOrdua.setCellValueFactory(new PropertyValueFactory<>("egunaOrdua"));
        prezioTotala.setCellValueFactory(new PropertyValueFactory<>("prezioTotala"));
        fakturaRuta.setCellValueFactory(new PropertyValueFactory<>("fakturaRuta"));
        langileakId.setCellValueFactory(new PropertyValueFactory<>("langileakId"));
        mahaiakId.setCellValueFactory(new PropertyValueFactory<>("mahaiakId"));
        ordainduta.setCellValueFactory(new PropertyValueFactory<>("ordainduta"));
        kargatu();
    }

    private void kargatu() {
        lista.clear();
        try (Connection conn = Konexioa.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM erreserbak")) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("eguna_ordua");
                lista.add(new Erreserba(
                        rs.getInt("id"),
                        rs.getString("bezero_izena"),
                        rs.getString("telefonoa"),
                        rs.getInt("pertsona_kopurua"),
                        ts != null ? ts.toLocalDateTime() : null,
                        rs.getObject("prezio_totala") != null ? rs.getDouble("prezio_totala") : null,
                        rs.getString("faktura_ruta"),
                        rs.getInt("langileak_id"),
                        rs.getInt("mahaiak_id"),
                        rs.getBoolean("ordainduta")
                ));
            }
            erreserbak.setItems(lista);

        } catch (SQLException e) {
            alerta(Alert.AlertType.ERROR, "Errorea", "DB errorea", "Ezin izan dira datuak kargatu");
        }
    }

    @FXML
    private void gehitu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gestapp/main/erreserbaGehitu-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(erreserbak.getScene().getWindow());
            stage.showAndWait();
            kargatu();
        } catch (IOException e) {
            alerta(Alert.AlertType.ERROR, "Errorea", "Leihoa", "Ezin izan da leihoa ireki");
        }
    }

    @FXML
    private void editatu() {
        Erreserba e = erreserbak.getSelectionModel().getSelectedItem();
        if (e == null) {
            alerta(Alert.AlertType.WARNING, "Kontuz", "Hautatu", "Erreserba bat hautatu behar da");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/erreserbaEditatu-view.fxml"));
            Parent root = loader.load();
            loader.<ErreserbakEditatuController>getController().setErreserba(e);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(erreserbak.getScene().getWindow());
            stage.showAndWait();
            kargatu();
        } catch (IOException ex) {
            alerta(Alert.AlertType.ERROR, "Errorea", "Leihoa", "Ezin izan da editatzeko leihoa ireki");
        }
    }

    @FXML
    private void ezabatu() {
        Erreserba e = erreserbak.getSelectionModel().getSelectedItem();
        if (e == null) {
            alerta(Alert.AlertType.WARNING, "Kontuz", "Hautatu", "Ezabatzeko erreserba bat hautatu");
            return;
        }

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM erreserbak WHERE id=?")) {

            ps.setInt(1, e.getId());
            ps.executeUpdate();
            alerta(Alert.AlertType.INFORMATION, "Ondo", "Ezabatuta", "Erreserba ezabatu da");
            kargatu();

        } catch (SQLException ex) {
            alerta(Alert.AlertType.ERROR, "Errorea", "DB errorea", "Ezin izan da erreserba ezabatu");
        }
    }

    private void alerta(Alert.AlertType mota, String t, String h, String m) {
        Alert a = new Alert(mota);
        a.initOwner(erreserbak.getScene().getWindow());
        a.initModality(Modality.APPLICATION_MODAL);
        a.setTitle(t);
        a.setHeaderText(h);
        a.setContentText(m);
        a.showAndWait();
    }

    @FXML
    private void atzera() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) erreserbak.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
