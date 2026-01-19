package com.gestapp.modeloa;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Erosketa {

    private final IntegerProperty id;
    private final IntegerProperty hornitzaileaId;
    private final IntegerProperty osagaiaId;
    private final IntegerProperty kantitatea;
    private final DoubleProperty prezioa;

    public Erosketa(int id, int hornitzaileaId, int osagaiaId,  int kantitatea, double prezioa) {
        this.id = new SimpleIntegerProperty(id);
        this.hornitzaileaId = new SimpleIntegerProperty(hornitzaileaId);
        this.osagaiaId = new SimpleIntegerProperty(osagaiaId);
        this.kantitatea = new SimpleIntegerProperty(kantitatea);
        this.prezioa = new SimpleDoubleProperty(prezioa);
    }

    public int getId() {
        return id.get();
    }

    public int getHornitzaileaId(){
        return hornitzaileaId.get();
    }

    public int getOsagaiaId(){
        return osagaiaId.get();
    }
    public int getKantitatea(){
        return kantitatea.get();
    }
    public double getPrezioa(){
        return prezioa.get();
    }

}
