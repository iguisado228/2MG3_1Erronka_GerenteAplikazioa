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

    private ObservableList<Erreserba> listaErreserbak = FXCollections.observableArrayList();

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
        datuak();
    }

    private void datuak() {
        listaErreserbak.clear();
        String sql = "SELECT * FROM erreserbak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("eguna_ordua");
                Erreserba e = new Erreserba(
                        rs.getInt("id"),
                        rs.getString("bezero_izena"),
                        rs.getString("telefonoa"),
                        rs.getInt("pertsona_kopurua"),
                        ts != null ? ts.toLocalDateTime() : null,
                        rs.getDouble("prezio_totala"),
                        rs.getString("faktura_ruta"),
                        rs.getInt("langileak_id"),
                        rs.getInt("mahaiak_id")
                );
                listaErreserbak.add(e);
            }

            erreserbak.setItems(listaErreserbak);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gehitu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/erreserbaGehitu-view.fxml"));
            Parent gehitu = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Erreserba Gehitu");
            stage.setScene(new Scene(gehitu));
            stage.initOwner(erreserbak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void editatu() {
        Erreserba e = erreserbak.getSelectionModel().getSelectedItem();
        if (e == null) {
            alerta("Errorea", "Ez dago ezer hautatuta", "Hautatu erreserba bat editatzeko");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/erreserbaEditatu-view.fxml"));
            Parent root = loader.load();
            ErreserbakEditatuController controller = loader.getController();
            controller.setErreserba(e);
            Stage stage = new Stage();
            stage.setTitle("Erreserba Editatu");
            stage.setScene(new Scene(root));
            stage.initOwner(erreserbak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void ezabatu() {
        Erreserba e = erreserbak.getSelectionModel().getSelectedItem();
        if (e == null) {
            alerta("Errorea", "Ez dago ezer hautatuta", "Hautatu erreserba bat ezabatzeko");
            return;
        }

        String sql = "DELETE FROM erreserbak WHERE id=?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, e.getId());
            pstmt.executeUpdate();
            datuak();
            alerta("Ondo", "Erreserba ezabatu da", "Hau da erreserba hautatua ezabatua");

        } catch (SQLException ex) {
            alerta("Errorea", "DB errorea", "Ezin izan da erreserba ezabatu");
        }
    }

    @FXML
    private void atzera() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) erreserbak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void alerta(String titulua, String header, String mezua) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulua);
        alert.setHeaderText(header);
        alert.setContentText(mezua);
        Stage owner = (Stage) erreserbak.getScene().getWindow();
        alert.initOwner(owner);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
    }
}
