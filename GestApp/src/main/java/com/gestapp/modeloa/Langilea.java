package com.gestapp.modeloa;

import javafx.beans.property.*;

public class Langilea {

    private final IntegerProperty id;
    private final IntegerProperty langileKodea;
    private final StringProperty izena;
    private final StringProperty abizena;
    private final StringProperty nan;
    private final StringProperty erabiltzaileIzena;
    private final StringProperty pasahitza;
    private final BooleanProperty gerentea;
    private final BooleanProperty tpvSarrera;
    private final StringProperty helbidea;

    public Langilea(int id, int langileKodea, String izena, String abizena,
                    String nan, String erabiltzaileIzena, String pasahitza,
                    boolean gerentea, boolean tpvSarrera, String helbidea) {
        this.id = new SimpleIntegerProperty(id);
        this.langileKodea = new SimpleIntegerProperty(langileKodea);
        this.izena = new SimpleStringProperty(izena);
        this.abizena = new SimpleStringProperty(abizena);
        this.nan = new SimpleStringProperty(nan);
        this.erabiltzaileIzena = new SimpleStringProperty(erabiltzaileIzena);
        this.pasahitza = new SimpleStringProperty(pasahitza);
        this.gerentea = new SimpleBooleanProperty(gerentea);
        this.tpvSarrera = new SimpleBooleanProperty(tpvSarrera);
        this.helbidea = new SimpleStringProperty(helbidea);
    }

    public int getId() { return id.get(); }
    public int getLangileKodea() { return langileKodea.get(); }
    public String getIzena() { return izena.get(); }
    public String getAbizena() { return abizena.get(); }
    public String getNan() { return nan.get(); }
    public String getErabiltzaileIzena() { return erabiltzaileIzena.get(); }
    public String getPasahitza() { return pasahitza.get(); }
    public boolean isGerentea() { return gerentea.get(); }
    public boolean isTpvSarrera() { return tpvSarrera.get(); }
    public String getHelbidea() { return helbidea.get(); }

    public BooleanProperty gerenteaProperty() { return gerentea; }
    public BooleanProperty tpvSarreraProperty() { return tpvSarrera; }

}
