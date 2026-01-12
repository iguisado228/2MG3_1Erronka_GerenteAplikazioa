package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Txanda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TxandakController {

    @FXML
    private TableView<Txanda> txandak;
    @FXML
    private TableColumn<Txanda, Integer> id;
    @FXML
    private TableColumn<Txanda, LocalDate> date;
    @FXML
    private TableColumn<Txanda, Integer> txandaZenbakia;
    @FXML
    private TableColumn<Txanda, Integer> langileaId;

    private ObservableList<Txanda> listaTxandak = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        txandaZenbakia.setCellValueFactory(new PropertyValueFactory<>("txandaZenbakia"));
        langileaId.setCellValueFactory(new PropertyValueFactory<>("langileaId"));

        date.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : formatter.format(item));
            }
        });

        datuak();
    }

    private void datuak() {
        listaTxandak.clear();
        String sql = "SELECT * FROM txandak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Date dbDate = rs.getDate("date");
                Txanda t = new Txanda(
                        rs.getInt("id"),
                        dbDate != null ? dbDate.toLocalDate() : null,
                        rs.getInt("txanda_zenbakia"),
                        rs.getInt("langileak_id")
                );
                listaTxandak.add(t);
            }

            txandak.setItems(listaTxandak);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gehitu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/txandaGehitu-view.fxml"));
            Parent gehitu = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Txanda Gehitu");
            stage.setScene(new Scene(gehitu));
            stage.initOwner(txandak.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void editatu() {
        Txanda t = txandak.getSelectionModel().getSelectedItem();
        if (t == null) {
            alerta("Errorea", "Ez dago ezer hautatuta", "Hautatu txanda bat editatzeko");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/txandaEditatu-view.fxml"));
            Parent root = loader.load();
            TxandakEditatuController controller = loader.getController();
            controller.setTxanda(t);
            Stage stage = new Stage();
            stage.setTitle("Txanda Editatu");
            stage.setScene(new Scene(root));
            stage.initOwner(txandak.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void ezabatu() {
        Txanda t = txandak.getSelectionModel().getSelectedItem();
        if (t == null) {
            alerta("Errorea", "Ez dago ezer hautatuta", "Hautatu txanda bat ezabatzeko");
            return;
        }

        String sql = "DELETE FROM txandak WHERE id=?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, t.getId());
            pstmt.executeUpdate();
            datuak();
            alerta("Ondo", "Txanda ezabatu da", "Txanda ezabatuta");

        } catch (SQLException ex) {
            alerta("Errorea", "DB errorea", "Ezin izan da txanda ezabatu");
        }
    }

    @FXML
    private void atzera() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txandak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void alerta(String titulua, String header, String mezua) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulua);
        alert.setHeaderText(header);
        alert.setContentText(mezua);
        Stage owner = (Stage) txandak.getScene().getWindow();
        alert.initOwner(owner);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}
