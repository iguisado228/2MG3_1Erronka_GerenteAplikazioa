package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Erreserba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ErreserbakEditatuController {

    @FXML private TextField txtBezeroIzena;
    @FXML private TextField txtTelefonoa;
    @FXML private TextField txtPertsonak;
    @FXML private DatePicker dpEguna;
    @FXML private TextField txtOrdua;
    @FXML private TextField txtPrezioa;
    @FXML private TextField txtFaktura;
    @FXML private ComboBox<LangileaItem> cmbLangilea;
    @FXML private ComboBox<MahaiItem> cmbMahai;
    @FXML private CheckBox chkOrdainduta;

    private Erreserba erreserba;

    public void initialize() {
        kargatuLangileak();
        kargatuMahaiak();
    }

    public void setErreserba(Erreserba e) {
        erreserba = e;
        txtBezeroIzena.setText(e.getBezeroIzena());
        txtTelefonoa.setText(e.getTelefonoa());
        txtPertsonak.setText(String.valueOf(e.getPertsonaKopurua()));
        dpEguna.setValue(e.getEgunaOrdua().toLocalDate());
        txtOrdua.setText(e.getEgunaOrdua().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        txtPrezioa.setText(e.getPrezioTotala() != null ? e.getPrezioTotala().toString() : "");
        txtFaktura.setText(e.getFakturaRuta());
        chkOrdainduta.setSelected(e.getOrdainduta());

        cmbLangilea.getItems().stream().filter(l -> l.id == e.getLangileakId()).findFirst().ifPresent(cmbLangilea::setValue);
        cmbMahai.getItems().stream().filter(m -> m.id == e.getMahaiakId()).findFirst().ifPresent(cmbMahai::setValue);
    }

    @FXML
    private void gorde() {
        String bezeroIzena = txtBezeroIzena.getText() != null ? txtBezeroIzena.getText().trim() : "";
        String telefonoa = txtTelefonoa.getText() != null ? txtTelefonoa.getText().trim() : "";
        String pertsonakStr = txtPertsonak.getText() != null ? txtPertsonak.getText().trim() : "";
        String orduaStr = txtOrdua.getText() != null ? txtOrdua.getText().trim() : "";

        if (bezeroIzena.isBlank() || telefonoa.isBlank() || pertsonakStr.isBlank() || orduaStr.isBlank()
                || dpEguna.getValue() == null || cmbLangilea.getValue() == null || cmbMahai.getValue() == null) {
            alerta("Errorea", "Datu falta", "Eremu derrigorrezko guztiak bete behar dira");
            return;
        }

        int pertsonak;
        try {
            pertsonak = Integer.parseInt(pertsonakStr);
        } catch (NumberFormatException e) {
            alerta("Errorea", "Zenbaki okerra", "Pertsona kopurua ez da zuzena");
            return;
        }

        Double prezioa = null;
        String prezioaStr = txtPrezioa.getText() != null ? txtPrezioa.getText().trim() : "";
        if (!prezioaStr.isBlank()) {
            try {
                prezioa = Double.parseDouble(prezioaStr);
            } catch (NumberFormatException e) {
                alerta("Errorea", "Zenbaki okerra", "Prezioa ez da zuzena");
                return;
            }
        }

        LocalTime ordua;
        try {
            ordua = LocalTime.parse(orduaStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            alerta("Errorea", "Ordua okerra", "HH:mm formatua erabili");
            return;
        }

        LocalDateTime dt = LocalDateTime.of(dpEguna.getValue(), ordua);

        String sql = "UPDATE erreserbak SET bezero_izena=?, telefonoa=?, pertsona_kopurua=?, eguna_ordua=?, prezio_totala=?, faktura_ruta=?, langileak_id=?, mahaiak_id=?, ordainduta=? WHERE id=?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bezeroIzena);
            pstmt.setString(2, telefonoa);
            pstmt.setInt(3, pertsonak);
            pstmt.setTimestamp(4, Timestamp.valueOf(dt));

            if (prezioa == null) pstmt.setNull(5, Types.DOUBLE);
            else pstmt.setDouble(5, prezioa);

            String faktura = txtFaktura.getText() != null ? txtFaktura.getText().trim() : "";
            if (faktura.isBlank()) pstmt.setNull(6, Types.VARCHAR);
            else pstmt.setString(6, faktura);

            pstmt.setInt(7, cmbLangilea.getValue().id);
            pstmt.setInt(8, cmbMahai.getValue().id);
            pstmt.setBoolean(9, chkOrdainduta.isSelected());
            pstmt.setInt(10, erreserba.getId());

            int affected = pstmt.executeUpdate();
            if (affected > 0) alerta("Ondo", "Erreserba eguneratu da", "Erreserba arrakastaz editatu da.");
            else alerta("Errorea", "Ezin izan da editatu", "Ezin izan da erreserba editatu.");

            ((Stage) txtBezeroIzena.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
            alerta("Errorea", "DB errorea", "Ezin izan da erreserba editatu");
        }
    }

    @FXML
    private void ezGorde() {
        ((Stage) txtBezeroIzena.getScene().getWindow()).close();
    }

    private void kargatuLangileak() {
        ObservableList<LangileaItem> lista = FXCollections.observableArrayList();
        try (Connection conn = Konexioa.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, izena FROM langileak")) {
            while (rs.next()) lista.add(new LangileaItem(rs.getInt("id"), rs.getString("izena")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbLangilea.setItems(lista);
    }

    private void kargatuMahaiak() {
        ObservableList<MahaiItem> lista = FXCollections.observableArrayList();
        try (Connection conn = Konexioa.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, kokapena FROM mahaiak")) {
            while (rs.next()) lista.add(new MahaiItem(rs.getInt("id"), rs.getString("kokapena")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cmbMahai.setItems(lista);
    }

    private void alerta(String t, String h, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        if (t.equalsIgnoreCase("Errorea")) a.setAlertType(Alert.AlertType.ERROR);
        a.initOwner((Stage) txtBezeroIzena.getScene().getWindow());
        a.initModality(Modality.APPLICATION_MODAL);
        a.setTitle(t);
        a.setHeaderText(h);
        a.setContentText(m);
        a.showAndWait();
    }

    private static class LangileaItem {
        int id;
        String izena;
        LangileaItem(int id, String izena) { this.id = id; this.izena = izena; }
        public String toString() { return izena; }
    }

    private static class MahaiItem {
        int id;
        String kokapena;
        MahaiItem(int id, String kokapena) { this.id = id; this.kokapena = kokapena; }
        public String toString() { return kokapena; }
    }
}
