package com.gestapp.modeloa;

import javafx.beans.property.*;

public class Osagaia
{
    private final IntegerProperty id;
    private final StringProperty izena;
    private final DoubleProperty prezioa;
    private final IntegerProperty stock;
    private final IntegerProperty hornitzaileaId;

    public Osagaia(int id, String izena,  double prezioa, int stock, int hornitzaileaId){
        this.id = new SimpleIntegerProperty(id);
        this.izena = new SimpleStringProperty(izena);
        this.prezioa = new SimpleDoubleProperty(prezioa);
        this.stock = new SimpleIntegerProperty(stock);
        this.hornitzaileaId = new SimpleIntegerProperty(hornitzaileaId);

    }


    public int getId(){
        return this.id.get();
    }

    public String getIzena(){
        return this.izena.get();
    }

    public double getPrezioa(){
        return this.prezioa.get();
    }
    public int getStock(){
        return this.stock.get();
    }
    public int getHornitzaileaId(){
        return this.hornitzaileaId.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }
    public void setIzena(String izena) {
        this.izena.set(izena);
    }

    public void setPrezioa(double prezioa) {
        this.prezioa.set(prezioa);
    }
    public void setStock(int stock) {
        this.stock.set(stock);
    }
}
