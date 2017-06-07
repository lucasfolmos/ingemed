package com.olmos.ingemed.aplication;

import android.app.Application;
import android.util.Log;

import com.olmos.ingemed.InterfaceDevice;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;
import com.olmos.ingemed.utils.BusProvider;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {
    private static BaseApplication instance;
    InterfaceDevice interfaceDevice;
    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;


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

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            int baudrate = 115200;
            String path = "/dev/ttyO1";

            Log.d("Debug", "Opening serial port: " + path + " bps: " + baudrate );
			/* Open the serial port */
            openSerialPort(path, baudrate);
        }
        return mSerialPort;
    }

    public void openSerialPort(String path, int baudrate) throws IOException {
        mSerialPort = new SerialPort(new File(path), baudrate, 0);
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
