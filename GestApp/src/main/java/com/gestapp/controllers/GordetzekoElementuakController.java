package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.GordetzekoElementua;
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

public class GordetzekoElementuakController {

    @FXML
    private TableView<GordetzekoElementua> elementuak;
    @FXML
    private TableColumn<GordetzekoElementua, Integer> id;
    @FXML
    private TableColumn<GordetzekoElementua, Integer> kantitatea;
    @FXML
    private TableColumn<GordetzekoElementua, Integer> hornitzaileaId;

    private final ObservableList<GordetzekoElementua> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        kantitatea.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));
        hornitzaileaId.setCellValueFactory(new PropertyValueFactory<>("hornitzaileaId"));
        datuak();
    }

    private void datuak() {
        lista.clear();
        String sql = "SELECT id, kantitatea, hornitzaileak_id AS hornitzaileaId FROM gordetzeko_elementuak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new GordetzekoElementua(
                        rs.getInt("id"),
                        rs.getInt("kantitatea"),
                        rs.getInt("hornitzaileaId")
                ));
            }
            elementuak.setItems(lista);

        } catch (SQLException e) {
            Stage owner = (Stage) elementuak.getScene().getWindow();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Errorea datuak kargatzean:\n" + e.getMessage());
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void gehitu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/gordetzekoElementuaGehitu-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner((Stage) elementuak.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            Stage owner = (Stage) elementuak.getScene().getWindow();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Leihoa irekitzean errorea:\n" + e.getMessage());
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void ezabatu() {
        GordetzekoElementua g = elementuak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) elementuak.getScene().getWindow();

        if (g == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Ez da elementurik aukeratu");
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
            return;
        }

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM gordetzeko_elementuak WHERE id=?")) {

            ps.setInt(1, g.getId());
            ps.executeUpdate();
            datuak();

            Alert ok = new Alert(Alert.AlertType.INFORMATION, "Elementua ezabatu da");
            ok.initOwner(owner);
            ok.initModality(Modality.APPLICATION_MODAL);
            ok.showAndWait();

        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Ezabatzean errorea:\n" + e.getMessage());
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void editatu() {
        GordetzekoElementua g = elementuak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) elementuak.getScene().getWindow();

        if (g == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Ez da elementurik aukeratu");
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/gordetzekoElementuaEditatu-view.fxml"));
            Parent root = loader.load();
            GordetzekoElementuakEditatuController c = loader.getController();
            c.setElementua(g);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(owner);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Editatzeko errorea:\n" + e.getMessage());
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void atzera() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Stage stage = (Stage) elementuak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) {
            Stage owner = (Stage) elementuak.getScene().getWindow();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Menua kargatzean errorea egon da:\n" + e.getMessage());
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }
}
