package com.gestapp.modeloa;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Txanda {

    private final IntegerProperty id;
    private final ObjectProperty<LocalDate> date;
    private final IntegerProperty txandaZenbakia;
    private final IntegerProperty langileaId;

    public Txanda(int id, LocalDate date, int txandaZenbakia, int langileaId) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleObjectProperty<>(date);
        this.txandaZenbakia = new SimpleIntegerProperty(txandaZenbakia);
        this.langileaId = new SimpleIntegerProperty(langileaId);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public int getTxandaZenbakia() {
        return txandaZenbakia.get();
    }

    public void setTxandaZenbakia(int txandaZenbakia) {
        this.txandaZenbakia.set(txandaZenbakia);
    }

    public IntegerProperty txandaZenbakiaProperty() {
        return txandaZenbakia;
    }

    public int getLangileaId() {
        return langileaId.get();
    }

    public void setLangileaId(int langileaId) {
        this.langileaId.set(langileaId);
    }

    public IntegerProperty langileaIdProperty() {
        return langileaId;
    }
}
