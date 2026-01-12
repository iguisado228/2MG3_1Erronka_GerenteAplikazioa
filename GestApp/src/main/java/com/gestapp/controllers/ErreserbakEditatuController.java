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

    private Erreserba erreserba;

    public void initialize() {
        kargatuLangileak();
        kargatuMahaiak();
    }

    public void setErreserba(Erreserba e) {
        this.erreserba = e;
        txtBezeroIzena.setText(e.getBezeroIzena());
        txtTelefonoa.setText(e.getTelefonoa());
        txtPertsonak.setText(String.valueOf(e.getPertsonaKopurua()));
        dpEguna.setValue(e.getEgunaOrdua().toLocalDate());
        txtOrdua.setText(e.getEgunaOrdua().toLocalTime().toString());
        txtPrezioa.setText(String.valueOf(e.getPrezioTotala()));
        txtFaktura.setText(e.getFakturaRuta());

        cmbLangilea.getItems().stream()
                .filter(l -> l.getId() == e.getLangileakId())
                .findFirst().ifPresent(cmbLangilea::setValue);

        cmbMahai.getItems().stream()
                .filter(m -> m.getId() == e.getMahaiakId())
                .findFirst().ifPresent(cmbMahai::setValue);
    }

    @FXML
    private void gorde() {
        if (dpEguna.getValue() == null) {
            alerta("Errorea", "Data falta da", "Hautatu data bat");
            return;
        }

        LocalTime ordua;
        try {
            ordua = LocalTime.parse(txtOrdua.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            alerta("Errorea", "Ordua formatu okerra", "Erabili HH:mm");
            return;
        }

        LocalDateTime dt = LocalDateTime.of(dpEguna.getValue(), ordua);

        String sql = """
            UPDATE erreserbak SET bezero_izena=?, telefonoa=?, pertsona_kopurua=?, eguna_ordua=?, prezio_totala=?, faktura_ruta=?, langileak_id=?, mahaiak_id=? WHERE id=?
            """;

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtBezeroIzena.getText());
            pstmt.setString(2, txtTelefonoa.getText());
            pstmt.setInt(3, Integer.parseInt(txtPertsonak.getText()));
            pstmt.setTimestamp(4, Timestamp.valueOf(dt));
            pstmt.setDouble(5, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(6, txtFaktura.getText());
            pstmt.setInt(7, cmbLangilea.getValue().getId());
            pstmt.setInt(8, cmbMahai.getValue().getId());
            pstmt.setInt(9, erreserba.getId());

            pstmt.executeUpdate();
            arrakasta("Ondo", "Erreserba editatua", "Aldaketak gorde dira");
            ((Stage) txtBezeroIzena.getScene().getWindow()).close();
        } catch (Exception e) {
            alerta("Errorea", "Errorea", "Ezin izan da erreserba editatu");
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
            while (rs.next()) {
                lista.add(new LangileaItem(rs.getInt("id"), rs.getString("izena")));
            }
        } catch (SQLException ignored) {}
        cmbLangilea.setItems(lista);
    }

    private void kargatuMahaiak() {
        ObservableList<MahaiItem> lista = FXCollections.observableArrayList();
        try (Connection conn = Konexioa.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, kokapena FROM mahaiak")) {
            while (rs.next()) {
                lista.add(new MahaiItem(rs.getInt("id"), rs.getString("kokapena")));
            }
        } catch (SQLException ignored) {}
        cmbMahai.setItems(lista);
    }

    private void alerta(String t, String h, String m) {
        Stage owner = (Stage) txtBezeroIzena.getScene().getWindow();
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.initOwner(owner);
        a.initModality(Modality.APPLICATION_MODAL);
        a.setTitle(t);
        a.setHeaderText(h);
        a.setContentText(m);
        a.showAndWait();
    }

    private void arrakasta(String t, String h, String m) {
        Stage owner = (Stage) txtBezeroIzena.getScene().getWindow();
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.initOwner(owner);
        a.initModality(Modality.APPLICATION_MODAL);
        a.setTitle(t);
        a.setHeaderText(h);
        a.setContentText(m);
        a.showAndWait();
    }

    private static class LangileaItem {
        private final int id;
        private final String izena;
        LangileaItem(int id, String izena) { this.id = id; this.izena = izena; }
        int getId() { return id; }
        public String toString() { return izena; }
    }

    private static class MahaiItem {
        private final int id;
        private final String kokapena;
        MahaiItem(int id, String kokapena) { this.id = id; this.kokapena = kokapena; }
        int getId() { return id; }
        public String toString() { return kokapena; }
    }
}
