package com.gestapp.modeloa;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Hornitzailea {

    private final IntegerProperty id;
    private final StringProperty izena;
    private final StringProperty kontaktua;
    private final StringProperty helbidea;


    public Hornitzailea (int id, String izena, String kontaktua, String helbidea){
        this.id = new SimpleIntegerProperty(id);
        this.izena = new SimpleStringProperty(izena);
        this.kontaktua = new SimpleStringProperty(kontaktua);
        this.helbidea = new SimpleStringProperty(this.helbidea);
    }

    public int getId(){
        return this.id.get();
    }

    public String getIzena(){
        return this.izena.get();
    }

    public String getKontaktua(){
        return this.kontaktua.get();
    }

    public String getHelbidea(){
        return this.helbidea.get();
    }


}
