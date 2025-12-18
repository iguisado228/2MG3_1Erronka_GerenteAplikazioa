package com.gestapp.modeloa;

import java.time.LocalDate;
import java.time.LocalTime;

public class Erreserba {
    private int id;
    private String bezeroIzena;
    private String telefonoa;
    private int pertsonaKopurua;
    private LocalDate eguna;
    private LocalTime ordua;
    private double prezioTotala;
    private String fakturaRuta;
    private int langileakId;
    private int mahaiakId;

    public Erreserba(int id, String bezeroIzena, String telefonoa, int pertsonaKopurua,
                     LocalDate eguna, LocalTime ordua, double prezioTotala,
                     String fakturaRuta, int langileakId, int mahaiakId) {

        this.id = id;
        this.bezeroIzena = bezeroIzena;
        this.telefonoa = telefonoa;
        this.pertsonaKopurua = pertsonaKopurua;
        this.eguna = eguna;
        this.ordua = ordua;
        this.prezioTotala = prezioTotala;
        this.fakturaRuta = fakturaRuta;
        this.langileakId = langileakId;
        this.mahaiakId = mahaiakId;
    }

    public int getId() { return id; }
    public String getBezeroIzena() { return bezeroIzena; }
    public String getTelefonoa() { return telefonoa; }
    public int getPertsonaKopurua() { return pertsonaKopurua; }
    public LocalDate getEguna() { return eguna; }
    public LocalTime getOrdua() { return ordua; }
    public double getPrezioTotala() { return prezioTotala; }
    public String getFakturaRuta() { return fakturaRuta; }
    public int getLangileakId() { return langileakId; }
    public int getMahaiakId() { return mahaiakId; }
}
