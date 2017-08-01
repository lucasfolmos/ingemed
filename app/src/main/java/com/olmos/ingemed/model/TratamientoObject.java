package com.olmos.ingemed.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TratamientoObject extends RealmObject {

    @PrimaryKey
    private String name;
    private long milisecondsResistivo;
    private long milisecondsCapacitivo;
    private long defaultMilisecondsCapacitivo;
    private long defaultMilisecondsResistivo;
    private boolean capacitivoPrimero;

    public boolean getCapacitivoPrimero() {
        return capacitivoPrimero;
    }

    public void setCapacitivoPrimero(boolean esCapacitivoPrimero) {
        capacitivoPrimero = esCapacitivoPrimero;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMilisecondsResistivo() {
        return milisecondsResistivo;
    }

    public void setMilisecondsResistivo(long milisecondsResistivo) {
        this.milisecondsResistivo = milisecondsResistivo;
    }

    public long getMilisecondsCapacitivo() {
        return milisecondsCapacitivo;
    }

    public void setMilisecondsCapacitivo(long milisecondsCapacitivo) {
        this.milisecondsCapacitivo = milisecondsCapacitivo;
    }

    public long getDefaultMilisecondsCapacitivo() {
        return defaultMilisecondsCapacitivo;
    }

    public void setDefaultMilisecondsCapacitivo(long defaultMilisecondsCapacitivo) {
        this.defaultMilisecondsCapacitivo = defaultMilisecondsCapacitivo;
    }

    public long getDefaultMilisecondsResistivo() {
        return defaultMilisecondsResistivo;
    }

    public void setDefaultMilisecondsResistivo(long defaultMilisecondsResistivo) {
        this.defaultMilisecondsResistivo = defaultMilisecondsResistivo;
    }
}
