package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Hornitzailea;
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

public class HornitzaileakController {

    @FXML
    private TableView<Hornitzailea> hornitzaileak;

    @FXML
    private TableColumn<Hornitzailea, Integer> id;

    @FXML
    private TableColumn<Hornitzailea, String> izena;

    @FXML
    private TableColumn<Hornitzailea, String> kontaktua;

    @FXML
    private TableColumn<Hornitzailea, String> helbidea;

    private final ObservableList<Hornitzailea> listaHornitzailea = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        izena.setCellValueFactory(new PropertyValueFactory<>("izena"));
        kontaktua.setCellValueFactory(new PropertyValueFactory<>("kontaktua"));
        helbidea.setCellValueFactory(new PropertyValueFactory<>("helbidea"));

        datuak();
    }

    private void datuak() {
        listaHornitzailea.clear();
        String query = "SELECT * FROM hornitzaileak";

        try (Connection conn = Konexioa.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Hornitzailea h = new Hornitzailea(
                        rs.getInt("id"),
                        rs.getString("izena"),
                        rs.getString("kontaktua"),
                        rs.getString("helbidea")


                );
                listaHornitzailea.add(h);
            }
            hornitzaileak.setItems(listaHornitzailea);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Errorea datuak irakurtzean");
        }


    }


    @FXML
    private void atzera() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("menu-view.fxml"));
            Stage stage = (Stage) hornitzaileak.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);

        }catch (IOException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Menua kargatzean errorea egon da");
        }



    }

    @FXML
    private void gehitu(){
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("com/gestapp/main/hornitzaileaGehitu-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Hornitzailea Gehitu");
        stage.setScene(new Scene(root));
        stage.initOwner((Stage) hornitzaileak.getScene().getWindow());
        stage.setResizable(false);
        stage.showAndWait();
        datuak();
    }catch (IOException e) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Errorea");
        alerta.setHeaderText(null);
        alerta.setContentText("Leihoa irekitzean errorea egon da");
        alerta.showAndWait();
    }

    }

    @FXML
    private void ezabatu(){
        Hornitzailea aukeratuta = hornitzaileak.getSelectionModel().getSelectedItem();
        Stage owner = (Stage) hornitzaileak.getScene().getWindow();

        if (aukeratuta == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.initOwner(owner);
            alerta.setTitle("Ezabatu");
            alerta.setHeaderText(null);
            alerta.setContentText("Ez da hornitzailerik aukeratu");
            alerta.showAndWait();
            return;
        }

        String sql = "DELETE from hornitzaileak WHERE id = ?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, aukeratuta.getId());
            pstmt.executeUpdate();
            datuak();

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.initOwner(owner);
            ok.setTitle("Ezabatu");
            ok.setHeaderText(null);
            ok.setContentText("Hornitzailea ezabatu da");
            ok.showAndWait();

        }catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.initOwner(owner);
            alerta.setTitle("Errorea");
            alerta.setHeaderText(null);
            alerta.setContentText("Ezabatzean errorea egon da");
        }
    }

    @FXML
    private void editatu(){
      Hornitzailea h = hornitzaileak.getSelectionModel().getSelectedItem();
      Stage owner = (Stage) hornitzaileak.getScene().getWindow();
      if (h == null) {
          Alert alerta = new Alert(Alert.AlertType.WARNING);
          alerta.initOwner(owner);
          alerta.setTitle("Editatu");
          alerta.setHeaderText(null);
          alerta.setContentText("Ez da hornitzailerik aukeratu");
          alerta.showAndWait();
          return;
      }

      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gestapp/main/hornitzaileaEditatu-view.fxml"));
          Parent root = loader.load();
          HornitzaileakEditatuController he = loader.getController();
          he.setHornitzailea(h);

          Stage stage = new Stage();
          stage.setTitle("Hornitzailea Editatu");
          stage.setScene(new Scene(root));
          stage.initOwner(owner);
          stage.setResizable(false);
          stage.showAndWait();
          datuak();
      }catch (IOException e) {
          Alert alerta = new Alert(Alert.AlertType.ERROR);
          alerta.initOwner(owner);
          alerta.setTitle("Errorea");
          alerta.setHeaderText(null);
          alerta.setContentText("Editatzeko errorea egon da");
          alerta.showAndWait();
      }

    }
}
