package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Eskaria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class EskariakController {

    @FXML private TableView<Eskaria> eskariak;
    @FXML private TableColumn<Eskaria, Integer> id;
    @FXML private TableColumn<Eskaria, Double> prezioa;
    @FXML private TableColumn<Eskaria, String> egoera;
    @FXML private TableColumn<Eskaria, Integer> erreserbakId;
    @FXML private TableColumn<Eskaria, Integer> langileaId;
    @FXML private TableColumn<Eskaria, Integer> mahaiaId;

    private ObservableList<Eskaria> listaEskariak = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        prezioa.setCellValueFactory(new PropertyValueFactory<>("prezioa"));
        egoera.setCellValueFactory(new PropertyValueFactory<>("egoera"));
        erreserbakId.setCellValueFactory(new PropertyValueFactory<>("erreserbak_id"));
        langileaId.setCellValueFactory(new PropertyValueFactory<>("erreserbak_langileak_id"));
        mahaiaId.setCellValueFactory(new PropertyValueFactory<>("erreserbak_mahaiak_id"));

        datuak();
    }

    private void datuak() {
        listaEskariak.clear();
        String query = "SELECT * FROM eskariak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Eskaria e = new Eskaria(
                        rs.getInt("id"),
                        rs.getDouble("prezioa"),
                        rs.getString("egoera"),
                        rs.getInt("erreserbak_id"),
                        rs.getString("erreserbak_langileak_id"),
                        rs.getString("erreserbak_mahaiak_id")
                );
                listaEskariak.add(e);
            }

            eskariak.setItems(listaEskariak);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gehitu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/eskariakGehitu-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Eskaria Gehitu");
            stage.setScene(new Scene(root));
            stage.initOwner(eskariak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();

            datuak();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void editatu() {
        Eskaria e = eskariak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) eskariak.getScene().getWindow();

        if (e == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.initOwner(owner);
            alerta.setTitle("Editatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da eskariarik hautatu.");
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/eskariakEditatu-view.fxml"));
            Parent root = loader.load();

            EskariakEditatuController controller = loader.getController();
            controller.setEskaria(e);

            Stage stage = new Stage();
            stage.setTitle("Eskaria Editatu");
            stage.setScene(new Scene(root));
            stage.initOwner(owner);
            stage.setResizable(false);
            stage.showAndWait();

            datuak();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void ezabatu() {
        Eskaria e = eskariak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) eskariak.getScene().getWindow();

        if (e == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.initOwner(owner);
            alerta.setTitle("Ezabatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da eskariarik hautatu.");
            alerta.showAndWait();
            return;
        }

        String sql = "DELETE FROM eskariak WHERE id=?";
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, e.getId());
            pstmt.executeUpdate();

            datuak();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ezabatu");
            ok.setHeaderText(null);
            ok.setContentText("Eskaria ongi ezabatu da.");
            ok.showAndWait();

        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    @FXML
    private void atzera() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) eskariak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
