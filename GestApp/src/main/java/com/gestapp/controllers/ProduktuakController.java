package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Produktua;
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

public class ProduktuakController {

    @FXML private TableView<Produktua> produktuak;
    @FXML private TableColumn<Produktua, Integer> id;
    @FXML private TableColumn<Produktua, String> izena;
    @FXML private TableColumn<Produktua, Double> prezioa;
    @FXML private TableColumn<Produktua, String> mota;
    @FXML private TableColumn<Produktua, Integer> stock;

    private ObservableList<Produktua> listaProduktua = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        izena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        prezioa.setCellValueFactory(new PropertyValueFactory<>("prezioa"));
        mota.setCellValueFactory(new PropertyValueFactory<>("mota"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        datuak();
    }

    private void datuak() {
        listaProduktua.clear();
        String query = "SELECT * FROM produktuak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Produktua p = new Produktua(
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getDouble("prezioa"),
                        rs.getString("mota"),
                        rs.getInt("stock")
                );
                listaProduktua.add(p);
            }

            produktuak.setItems(listaProduktua);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void atzera() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/menu-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) produktuak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void gehitu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/produktuaGehitu-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Produktua Gehitu");
            stage.setScene(new Scene(root));
            stage.initOwner(produktuak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();

            datuak();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ezabatu() {
        Produktua aukeratuta = produktuak.getSelectionModel().getSelectedItem();

        if (aukeratuta == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Ezabatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da produkturik hautatu.");
            alerta.showAndWait();
            return;
        }

        String sql = "DELETE FROM produktuak WHERE id=?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, aukeratuta.getId());
            pstmt.executeUpdate();

            datuak();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Ezabatu");
            ok.setHeaderText(null);
            ok.setContentText("Produktua ongi ezabatu da.");
            ok.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editatu() {
        Produktua p = produktuak.getSelectionModel().getSelectedItem();

        if (p == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Editatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da produkturik hautatu.");
            alerta.show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/produktuaEditatu-view.fxml"));
            Parent root = loader.load();

            ProduktuakEditatuController controller = loader.getController();
            controller.setProduktua(p);

            Stage stage = new Stage();
            stage.setTitle("Produktua Editatu");
            stage.setScene(new Scene(root));
            stage.initOwner(produktuak.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();

            datuak();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
