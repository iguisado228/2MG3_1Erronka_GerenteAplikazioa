package com.gestapp.modeloa;

import javafx.beans.property.*;

public class GordetzekoElementua {

    private final IntegerProperty id;
    private final IntegerProperty kantitatea;
    private final IntegerProperty hornitzaileaId;

    public GordetzekoElementua(int id, int kantitatea, int hornitzaileaId) {
        this.id = new SimpleIntegerProperty(id);
        this.kantitatea = new SimpleIntegerProperty(kantitatea);
        this.hornitzaileaId = new SimpleIntegerProperty(hornitzaileaId);
    }

    public int getId() { return id.get(); }
    public int getKantitatea() { return kantitatea.get(); }
    public int getHornitzaileaId() { return hornitzaileaId.get(); }
}
