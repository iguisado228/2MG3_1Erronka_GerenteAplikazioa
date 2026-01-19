package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Mahaia;
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

public class MahaiakController {

    @FXML
    private TableView<Mahaia> mahaiak;
    @FXML
    private TableColumn<Mahaia, Integer> id;
    @FXML
    private TableColumn<Mahaia, Integer> zenbakia;
    @FXML
    private TableColumn<Mahaia, Integer> pertsonaKopurua;
    @FXML
    private TableColumn<Mahaia, String> kokapena;

    private final ObservableList<Mahaia> listaMahaia = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        zenbakia.setCellValueFactory(new PropertyValueFactory<>("zenbakia"));
        pertsonaKopurua.setCellValueFactory(new PropertyValueFactory<>("pertsonaKopurua"));
        kokapena.setCellValueFactory(new PropertyValueFactory<>("kokapena"));
        datuak();
    }

    private void datuak() {
        listaMahaia.clear();
        String query = "SELECT * FROM mahaiak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Mahaia m = new Mahaia(
                        rs.getInt("id"),
                        rs.getInt("zenbakia"),
                        rs.getInt("pertsona_kopurua"),
                        rs.getString("kokapena")
                );
                listaMahaia.add(m);
            }
            mahaiak.setItems(listaMahaia);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Errorea datuak irakurtzean");
            alert.showAndWait();
        }
    }

    @FXML
    private void atzera() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Stage stage = (Stage) mahaiak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Menua kargatzean errorea egon da");
            alert.showAndWait();
        }
    }

    @FXML
    private void gehitu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/mahaiaGehitu-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Mahaia Gehitu");
            stage.setScene(new Scene(root));
            stage.initOwner(mahaiak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Leihoa irekitzean errorea egon da");
            alert.showAndWait();
        }
    }

    @FXML
    private void ezabatu() {
        Mahaia aukeratuta = mahaiak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) mahaiak.getScene().getWindow();

        if (aukeratuta == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.initOwner(owner);
            alerta.setTitle("Ezabatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da mahairik aukeratu");
            alerta.showAndWait();
            return;
        }

        String sql = "DELETE FROM mahaiak WHERE id=?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, aukeratuta.getId());
            pstmt.executeUpdate();
            datuak();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ezabatu");
            ok.setHeaderText(null);
            ok.setContentText("Mahaia ondo ezabatu da");
            ok.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(owner);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Ezabatzeko errorea egon da");
            alert.showAndWait();
        }
    }

    @FXML
    private void editatu() {
        Mahaia m = mahaiak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) mahaiak.getScene().getWindow();

        if (m == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.initOwner(owner);
            alerta.setTitle("Editatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da mahairik hautatu");
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/mahaiaEditatu-view.fxml"));
            Parent root = loader.load();
            MahaiakEditatuController controller = loader.getController();
            controller.setMahaia(m);

            Stage stage = new Stage();
            stage.setTitle("Mahaia Editatu");
            stage.setScene(new Scene(root));
            stage.initOwner(owner);
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(owner);
            alert.setTitle("Errorea");
            alert.setHeaderText(null);
            alert.setContentText("Editatzeko errorea egon da");
            alert.showAndWait();
        }
    }
}
