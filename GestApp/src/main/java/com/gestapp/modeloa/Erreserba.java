package com.gestapp.modeloa;

import java.time.LocalDateTime;

public class Erreserba {

    private int id;
    private String bezeroIzena;
    private String telefonoa;
    private int pertsonaKopurua;
    private LocalDateTime egunaOrdua;
    private Double prezioTotala;
    private String fakturaRuta;
    private int langileakId;
    private int mahaiakId;
    private boolean ordainduta;

    public Erreserba(int id, String bezeroIzena, String telefonoa, int pertsonaKopurua,
                     LocalDateTime egunaOrdua, Double prezioTotala, String fakturaRuta,
                     int langileakId, int mahaiakId, boolean ordainduta) {
        this.id = id;
        this.bezeroIzena = bezeroIzena;
        this.telefonoa = telefonoa;
        this.pertsonaKopurua = pertsonaKopurua;
        this.egunaOrdua = egunaOrdua;
        this.prezioTotala = prezioTotala;
        this.fakturaRuta = fakturaRuta;
        this.langileakId = langileakId;
        this.mahaiakId = mahaiakId;
        this.ordainduta = ordainduta;
    }

    public int getId() { return id; }
    public String getBezeroIzena() { return bezeroIzena; }
    public String getTelefonoa() { return telefonoa; }
    public int getPertsonaKopurua() { return pertsonaKopurua; }
    public LocalDateTime getEgunaOrdua() { return egunaOrdua; }
    public Double getPrezioTotala() { return prezioTotala; }
    public String getFakturaRuta() { return fakturaRuta; }
    public int getLangileakId() { return langileakId; }
    public int getMahaiakId() { return mahaiakId; }
    public boolean getOrdainduta() { return ordainduta; }
}
