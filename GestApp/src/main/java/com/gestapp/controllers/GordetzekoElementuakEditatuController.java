package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.GordetzekoElementua;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class GordetzekoElementuakEditatuController {

    @FXML
    private TextField txtKantitatea;
    @FXML
    private ComboBox<HornitzaileaItem> cmbHornitzailea;

    private GordetzekoElementua elementua;
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

    public void setElementua(GordetzekoElementua g) {
        this.elementua = g;
        txtKantitatea.setText(String.valueOf(g.getKantitatea()));

        for (HornitzaileaItem h : lista) {
            if (h.getId() == g.getHornitzaileaId()) {
                cmbHornitzailea.setValue(h);
                break;
            }
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

            String sql = "UPDATE gordetzeko_elementuak SET kantitatea=?, hornitzaileak_id=? WHERE id=?";

            try (Connection conn = Konexioa.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, kantitatea);
                ps.setInt(2, cmbHornitzailea.getValue().getId());
                ps.setInt(3, elementua.getId());
                ps.executeUpdate();

                Alert ok = new Alert(Alert.AlertType.INFORMATION, "Elementua eguneratu da");
                ok.initOwner(owner);
                ok.initModality(Modality.APPLICATION_MODAL);
                ok.showAndWait();
                owner.close();

            } catch (SQLException e) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Errorea eguneratzean:\n" + e.getMessage());
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
    private void ezGorde() {
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
