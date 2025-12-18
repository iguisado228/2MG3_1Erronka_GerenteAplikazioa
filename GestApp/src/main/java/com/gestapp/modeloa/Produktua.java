package com.gestapp.modeloa;

public class Produktua {
    private int id;
    private String izena;
    private double prezioa;
    private String mota;
    private int stock;

    public Produktua(int id, String izena, double prezioa, String mota, int stock) {
        this.id = id;
        this.izena = izena;
        this.prezioa = prezioa;
        this.mota = mota;
        this.stock = stock;
    }


    public int getId() { return id; }
    public String getIzena() { return izena; }
    public double getPrezioa() { return prezioa; }
    public String getMota() { return mota; }
    public int getStock() { return stock; }

    public void setIzena(String izena) { this.izena = izena; }
    public void setPrezioa(double prezioa) { this.prezioa = prezioa; }
    public void setMota(String mota) { this.mota = mota; }
    public void setStock(int stock) { this.stock = stock; }
}
