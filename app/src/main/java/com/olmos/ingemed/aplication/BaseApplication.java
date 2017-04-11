package com.olmos.ingemed.aplication;

import android.app.Application;

import com.olmos.ingemed.InterfaceDevice;
import com.olmos.ingemed.utils.BusProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {


    private static BaseApplication instance;
    InterfaceDevice interfaceDevice;


    public BaseApplication() {
        instance = this;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        // Use the config
        Realm.setDefaultConfiguration(config);

        interfaceDevice = new InterfaceDevice(BusProvider.getInstance());
    }

    public InterfaceDevice getInterfaceDevice() {
        return interfaceDevice;
    }

    public void setInterfaceDevice(InterfaceDevice interfaceDevice) {
        this.interfaceDevice = interfaceDevice;
    }

    public void startModoManual(long milliseconds, int potenciaActual, boolean isResistivo) {

    }

    public void startModoPrograma(long mTiempoCapacitivo, long mTiempoResistivo, int potencia) {

    }

    public void play() {

    }
}
