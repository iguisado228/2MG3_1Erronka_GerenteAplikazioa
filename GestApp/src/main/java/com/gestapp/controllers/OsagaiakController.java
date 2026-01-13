package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Osagaia;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class OsagaiakController {

    @FXML
    private TableView<Osagaia> osagaiak;

    @FXML
    private TableColumn<Osagaia, Integer> id;

    @FXML
    private TableColumn<Osagaia, String> izena;

    @FXML
    private TableColumn<Osagaia, Double> prezioa;

    @FXML
    private TableColumn<Osagaia, Integer> stock;

    @FXML
    private TableColumn<Osagaia, Integer> hornitzaileaId;

    private final ObservableList<Osagaia> listaOsagaia = FXCollections.observableArrayList();

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
        listaOsagaia.clear();
        String query = "SELECT id, izena, prezioa, stock, hornitzaileak_id AS hornitzaileaId FROM osagaiak ";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Osagaia o = new Osagaia(
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getDouble("prezioa"),
                        rs.getInt("stock"),
                        rs.getInt("hornitzaileaId")
                );
                listaOsagaia.add(o);
            }
            osagaiak.setItems(listaOsagaia);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setHeaderText(null);
            alerta.setContentText("Errorea datuak kargatzean");
            alerta.showAndWait();
        }

    }

    @FXML
    private void atzera() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Stage stage = (Stage) osagaiak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Errorea menua kargatzean");
            alerta.showAndWait();
        }

    }

    @FXML
    private void gehitu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/osagaiaGehitu-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Osagaia gehitu");
            stage.setScene(new Scene(root));
            stage.initOwner((Stage) osagaiak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        }catch (IOException e){
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Leihoa irekitzean errorea egon da");
            alerta.showAndWait();
        }
    }

    @FXML
    private void ezabatu(){
        Osagaia aukeratuta = osagaiak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) osagaiak.getScene().getWindow();

        if (aukeratuta == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.initOwner(owner);
            alerta.setTitle("Ezabatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da osagairik aukeratu");
            alerta.showAndWait();
            return;
        }

        String sql = "DELETE FROM osagaiak WHERE id = ?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, aukeratuta.getId());
            pstmt.executeUpdate();
            datuak();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ezabatu");
            ok.setHeaderText(null);
            ok.setContentText("Osagaia ezabatu da");
            ok.showAndWait();

        }catch (SQLException e){
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(owner);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Ezabatzean errorea egon da");
            alerta.showAndWait();
        }
    }

    @FXML
    private void editatu(){
        Osagaia o = osagaiak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) osagaiak.getScene().getWindow();

        if (o == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.initOwner(owner);
            alerta.setTitle("Editatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da osagairik aukeratu");
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/osagaiaEditatu-view.fxml"));
            Parent root = loader.load();
            OsagaiakEditatuController oe = loader.getController();
            oe.setOsagaia(o);

            Stage stage = new Stage();
            stage.setTitle("Osagaia Editatu");
            stage.setScene(new Scene(root));
            stage.initOwner(owner);
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(owner);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Editatzeko errorea egon da");
            alerta.showAndWait();
        }
    }
}
