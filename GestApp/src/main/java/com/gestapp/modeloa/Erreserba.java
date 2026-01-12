package com.gestapp.modeloa;

import java.time.LocalDateTime;

public class Erreserba {
    private int id;
    private String bezeroIzena;
    private String telefonoa;
    private int pertsonaKopurua;
    private LocalDateTime egunaOrdua;
    private double prezioTotala;
    private String fakturaRuta;
    private int langileakId;
    private int mahaiakId;

    public Erreserba(int id, String bezeroIzena, String telefonoa, int pertsonaKopurua,
                     LocalDateTime egunaOrdua, double prezioTotala,
                     String fakturaRuta, int langileakId, int mahaiakId) {
        this.id = id;
        this.bezeroIzena = bezeroIzena;
        this.telefonoa = telefonoa;
        this.pertsonaKopurua = pertsonaKopurua;
        this.egunaOrdua = egunaOrdua;
        this.prezioTotala = prezioTotala;
        this.fakturaRuta = fakturaRuta;
        this.langileakId = langileakId;
        this.mahaiakId = mahaiakId;
    }

    public int getId() { return id; }
    public String getBezeroIzena() { return bezeroIzena; }
    public String getTelefonoa() { return telefonoa; }
    public int getPertsonaKopurua() { return pertsonaKopurua; }
    public LocalDateTime getEgunaOrdua() { return egunaOrdua; }
    public double getPrezioTotala() { return prezioTotala; }
    public String getFakturaRuta() { return fakturaRuta; }
    public int getLangileakId() { return langileakId; }
    public int getMahaiakId() { return mahaiakId; }
}
