package com.gestapp.controllers;

import com.gestapp.konexioa.Konexioa;
import com.gestapp.modeloa.Erreserba;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ErreserbakEditatuController {

    @FXML private TextField txtBezeroIzena;
    @FXML private TextField txtTelefonoa;
    @FXML private TextField txtPertsonak;
    @FXML private DatePicker dpEguna;
    @FXML private TextField txtOrdua;
    @FXML private TextField txtPrezioa;
    @FXML private TextField txtFaktura;
    @FXML private TextField txtLangileId;
    @FXML private TextField txtMahaiId;

    private Erreserba erreserba;

    public void setErreserba(Erreserba e) {
        this.erreserba = e;
        txtBezeroIzena.setText(e.getBezeroIzena());
        txtTelefonoa.setText(e.getTelefonoa());
        txtPertsonak.setText(String.valueOf(e.getPertsonaKopurua()));
        dpEguna.setValue(e.getEguna());
        txtOrdua.setText(String.valueOf(e.getOrdua()));
        txtPrezioa.setText(String.valueOf(e.getPrezioTotala()));
        txtFaktura.setText(e.getFakturaRuta());
        txtLangileId.setText(String.valueOf(e.getLangileakId()));
        txtMahaiId.setText(String.valueOf(e.getMahaiakId()));
    }

    @FXML
    private void gorde() {
        String sql = "UPDATE erreserbak SET bezero_izena=?, telefonoa=?, pertsona_kopurua=?, eguna=?, ordua=?, prezio_totala=?, faktura_ruta=?, langileak_id=?, mahaiak_id=? WHERE id=?";

        try (Connection conn = Konexioa.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtBezeroIzena.getText());
            pstmt.setString(2, txtTelefonoa.getText());
            pstmt.setInt(3, Integer.parseInt(txtPertsonak.getText()));
            pstmt.setDate(4, java.sql.Date.valueOf(dpEguna.getValue()));
            pstmt.setTime(5, java.sql.Time.valueOf(txtOrdua.getText()));
            pstmt.setDouble(6, Double.parseDouble(txtPrezioa.getText()));
            pstmt.setString(7, txtFaktura.getText());
            pstmt.setInt(8, Integer.parseInt(txtLangileId.getText()));
            pstmt.setInt(9, Integer.parseInt(txtMahaiId.getText()));
            pstmt.setInt(10, erreserba.getId());

            pstmt.executeUpdate();
            ((Stage) txtBezeroIzena.getScene().getWindow()).close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ezGorde() {
        ((Stage) txtBezeroIzena.getScene().getWindow()).close();
    }
}
