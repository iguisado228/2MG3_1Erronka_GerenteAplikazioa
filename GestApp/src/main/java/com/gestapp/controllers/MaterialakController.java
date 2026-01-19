package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Materiala;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class MaterialakController {

    @FXML
    private TableView<Materiala> materialak;
    @FXML
    private TableColumn<Materiala, Integer> id;
    @FXML
    private TableColumn<Materiala, String> izena;
    @FXML
    private TableColumn<Materiala, Double> prezioa;
    @FXML
    private TableColumn<Materiala, Integer> stock;
    @FXML
    private TableColumn<Materiala, Integer> hornitzaileaId;

    private final ObservableList<Materiala> listaMateriala = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        izena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        prezioa.setCellValueFactory(new PropertyValueFactory<>("prezioa"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        hornitzaileaId.setCellValueFactory(new PropertyValueFactory<>("hornitzaileaId"));
        datuak();
    }

    private void datuak() {
        listaMateriala.clear();
        String query = "SELECT id, izena, prezioa, stock, hornitzaileak_id AS hornitzaileaId FROM materialak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                listaMateriala.add(new Materiala(
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getDouble("prezioa"),
                        rs.getInt("stock"),
                        rs.getInt("hornitzaileaId")
                ));
            }
            materialak.setItems(listaMateriala);

        } catch (SQLException e) {
            Stage jabe = (Stage) materialak.getScene().getWindow();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Errorea datuak kargatzean");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void atzera() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Stage stage = (Stage) materialak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) {
            Stage jabe = (Stage) materialak.getScene().getWindow();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Errorea menua kargatzean");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void gehitu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gestapp/main/materialaGehitu-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner((Stage) materialak.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            Stage jabe = (Stage) materialak.getScene().getWindow();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Leihoa irekitzean errorea");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void ezabatu() {
        Stage jabe = (Stage) materialak.getScene().getWindow();
        Materiala m = materialak.getSelectionModel().getSelectedItem();

        if (m == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Ez da materialik aukeratu");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
            return;
        }

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM materialak WHERE id=?")) {

            pstmt.setInt(1, m.getId());
            pstmt.executeUpdate();
            datuak();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Materiala ezabatu da");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();

        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Ezabatzean errorea");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void editatu() {
        Stage jabe = (Stage) materialak.getScene().getWindow();
        Materiala m = materialak.getSelectionModel().getSelectedItem();

        if (m == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Ez da materialik aukeratu");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/materialaEditatu-view.fxml"));
            Parent root = loader.load();
            MaterialakEditatuController c = loader.getController();
            c.setMateriala(m);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(jabe);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Editatzeko errorea");
            alerta.initOwner(jabe);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }
}
