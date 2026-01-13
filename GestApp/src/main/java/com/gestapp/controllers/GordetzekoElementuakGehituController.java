package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class GordetzekoElementuakGehituController {

    @FXML
    private TextField txtKantitatea;
    @FXML
    private ComboBox<HornitzaileaItem> cmbHornitzailea;

    private final ObservableList<HornitzaileaItem> lista = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        try (Connection conn = Konexioa.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, izena FROM hornitzaileak");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new HornitzaileaItem(
                        rs.getInt("id"),
                        rs.getString("izena")
                ));
            }
            cmbHornitzailea.setItems(lista);

        } catch (SQLException e) {
            Stage owner = (Stage) txtKantitatea.getScene().getWindow();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Errorea hornitzaileak kargatzean:\n" + e.getMessage());
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void gorde() {
        Stage owner = (Stage) txtKantitatea.getScene().getWindow();

        if (cmbHornitzailea.getValue() == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Aukeratu hornitzailea");
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
            return;
        }

        try {
            int kantitatea = Integer.parseInt(txtKantitatea.getText());

            String sql = "INSERT INTO gordetzeko_elementuak (kantitatea, hornitzaileak_id) VALUES (?, ?)";

            try (Connection conn = Konexioa.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, kantitatea);
                ps.setInt(2, cmbHornitzailea.getValue().getId());
                ps.executeUpdate();

                Alert ok = new Alert(Alert.AlertType.INFORMATION, "Elementua ondo gorde da");
                ok.initOwner(owner);
                ok.initModality(Modality.APPLICATION_MODAL);
                ok.showAndWait();
                owner.close();

            } catch (SQLException e) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Errorea gordetzean:\n" + e.getMessage());
                alerta.initOwner(owner);
                alerta.initModality(Modality.APPLICATION_MODAL);
                alerta.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Kantitatea zenbaki bat izan behar da");
            alerta.initOwner(owner);
            alerta.initModality(Modality.APPLICATION_MODAL);
            alerta.showAndWait();
        }
    }

    @FXML
    private void atzera() {
        ((Stage) txtKantitatea.getScene().getWindow()).close();
    }

    public static class HornitzaileaItem {
        private final int id;
        private final String izena;

        public HornitzaileaItem(int id, String izena) {
            this.id = id;
            this.izena = izena;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return izena;
        }
    }
}
