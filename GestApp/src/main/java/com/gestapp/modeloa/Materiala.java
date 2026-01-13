package com.gestapp.modeloa;

import javafx.beans.property.*;

public class Materiala {

    private final IntegerProperty id;
    private final StringProperty izena;
    private final DoubleProperty prezioa;
    private final IntegerProperty stock;
    private final IntegerProperty hornitzaileaId;

    public Materiala(int id, String izena, double prezioa, int stock, int hornitzaileaId) {
        this.id = new SimpleIntegerProperty(id);
        this.izena = new SimpleStringProperty(izena);
        this.prezioa = new SimpleDoubleProperty(prezioa);
        this.stock = new SimpleIntegerProperty(stock);
        this.hornitzaileaId = new SimpleIntegerProperty(hornitzaileaId);
    }

    public int getId() { return id.get(); }
    public String getIzena() { return izena.get(); }
    public double getPrezioa() { return prezioa.get(); }
    public int getStock() { return stock.get(); }
    public int getHornitzaileaId() { return hornitzaileaId.get(); }
}
