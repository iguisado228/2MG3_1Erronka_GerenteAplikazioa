package com.gestapp.modeloa;

public class Eskaria {
    private int id;
    private double prezioa;
    private String egoera;
    private int erreserbak_id;
    private String langileIzena;
    private String mahaiaKokapena;

    public Eskaria(int id, double prezioa, String egoera, int erreserbak_id, String langileIzena, String mahaiaKokapena) {
        this.id = id;
        this.prezioa = prezioa;
        this.egoera = egoera;
        this.erreserbak_id = erreserbak_id;
        this.langileIzena = langileIzena;
        this.mahaiaKokapena = mahaiaKokapena;
    }

    public int getId() { return id; }
    public double getPrezioa() { return prezioa; }
    public String getEgoera() { return egoera; }
    public int getErreserbak_id() { return erreserbak_id; }
    public String getLangileIzena() { return langileIzena; }
    public String getMahaiakKokapena() { return mahaiaKokapena; }

    public void setEgoera(String egoera) { this.egoera = egoera; }
    public void setErreserbak_id(int erreserbak_id) { this.erreserbak_id = erreserbak_id; }
    public void setLangileIzena(String langileIzena) { this.langileIzena = langileIzena; }
    public void setMahaiakKokapena(String mahaiaKokapena) { this.mahaiaKokapena = mahaiaKokapena; }
}
