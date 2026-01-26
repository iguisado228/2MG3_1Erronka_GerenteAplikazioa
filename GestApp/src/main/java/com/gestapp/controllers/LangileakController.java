package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Langilea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LangileakController {

    @FXML private TableView<Langilea> langileak;
    @FXML private TableColumn<Langilea, Integer> id;
    @FXML private TableColumn<Langilea, Integer> langileKodea;
    @FXML private TableColumn<Langilea, String> izena;
    @FXML private TableColumn<Langilea, String> abizena;
    @FXML private TableColumn<Langilea, String> nan;
    @FXML private TableColumn<Langilea, String> erabiltzaileIzena;
    @FXML private TableColumn<Langilea, String> pasahitza;
    @FXML private TableColumn<Langilea, Boolean> gerentea;
    @FXML private TableColumn<Langilea, Boolean> tpvSarrera;
    @FXML private TableColumn<Langilea, String> helbidea;

    private final ObservableList<Langilea> listaLangileak = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        langileKodea.setCellValueFactory(new PropertyValueFactory<>("langileKodea"));
        izena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        abizena.setCellValueFactory(new PropertyValueFactory<>("abizena"));
        nan.setCellValueFactory(new PropertyValueFactory<>("nan"));
        erabiltzaileIzena.setCellValueFactory(new PropertyValueFactory<>("erabiltzaileIzena"));
        pasahitza.setCellValueFactory(new PropertyValueFactory<>("pasahitza"));
        gerentea.setCellValueFactory(cellData -> cellData.getValue().gerenteaProperty());
        gerentea.setCellFactory(tc -> new CheckBoxTableCell<>());
        tpvSarrera.setCellValueFactory(cellData -> cellData.getValue().tpvSarreraProperty());
        tpvSarrera.setCellFactory(tc -> new CheckBoxTableCell<>());
        helbidea.setCellValueFactory(new PropertyValueFactory<>("helbidea"));
        datuak();
    }

    private void datuak() {
        listaLangileak.clear();
        String sql = "SELECT * FROM langileak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Langilea langilea = new Langilea(
                        rs.getInt("id"),
                        rs.getInt("langile_kodea"),
                        rs.getString("izena"),
                        rs.getString("abizena"),
                        rs.getString("nan"),
                        rs.getString("erabiltzaile_izena"),
                        rs.getString("pasahitza"),
                        rs.getBoolean("gerentea"),
                        rs.getBoolean("tpv_sarrera"),
                        rs.getString("helbidea")
                );
                listaLangileak.add(langilea);
            }
            langileak.setItems(listaLangileak);

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Errorea");
            error.setHeaderText(null);
            error.setContentText("Errorea langileak kargatzean.");
            error.show();
            ((Stage) error.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
        }
    }

    @FXML
    private void atzera() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) langileak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gehitu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/langileaGehitu-view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Langilea Gehitu");
            stage.setScene(new Scene(root));
            stage.initOwner(langileak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editatu() {
        Langilea aukeratuta = langileak.getSelectionModel().getSelectedItem();
        if (aukeratuta == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Editatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da langilerik hautatu.");
            alerta.show();
            ((Stage) alerta.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/langileaEditatu-view.fxml"));
            Parent root = loader.load();
            LangileaEditatuController controller = loader.getController();
            controller.setLangilea(aukeratuta);
            Stage stage = new Stage();
            stage.setTitle("Langilea Editatu");
            stage.setScene(new Scene(root));
            stage.initOwner(langileak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();
            datuak();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ezabatu() {
        Langilea aukeratuta = langileak.getSelectionModel().getSelectedItem();
        if (aukeratuta == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Ezabatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da langilerik hautatu.");
            alerta.initOwner(langileak.getScene().getWindow());
            alerta.show();
            ((Stage) alerta.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
            return;
        }

        String sql = "DELETE FROM langileak WHERE id = ?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, aukeratuta.getId());
            pstmt.executeUpdate();
            datuak();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Ezabatu");
            ok.setHeaderText(null);
            ok.setContentText("Langilea ongi ezabatu da.");
            ok.initOwner(langileak.getScene().getWindow());
            ok.show();
            ((Stage) ok.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);

        } catch (SQLException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Errorea");
            error.setHeaderText(null);
            error.setContentText("Errorea langilea ezabatzean.");
            error.initOwner(langileak.getScene().getWindow());
            error.show();
            ((Stage) error.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
        }
    }

}
