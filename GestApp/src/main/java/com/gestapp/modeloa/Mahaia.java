package com.gestapp.modeloa;

import javafx.beans.property.*;

public class Mahaia {

    private final IntegerProperty id;
    private final IntegerProperty zenbakia;
    private final IntegerProperty pertsonaKopurua;
    private final StringProperty kokapena;

    public Mahaia(int id, int zenbakia, int pertsonaKopurua, String kokapena) {
        this.id = new SimpleIntegerProperty(id);
        this.zenbakia = new SimpleIntegerProperty(zenbakia);
        this.pertsonaKopurua = new SimpleIntegerProperty(pertsonaKopurua);
        this.kokapena = new SimpleStringProperty(kokapena);
    }

    public int getId() {
        return id.get();
    }

    public int getZenbakia() {
        return zenbakia.get();
    }

    public int getPertsonaKopurua() {
        return pertsonaKopurua.get();
    }

    public String getKokapena() {
        return kokapena.get();
    }
}
