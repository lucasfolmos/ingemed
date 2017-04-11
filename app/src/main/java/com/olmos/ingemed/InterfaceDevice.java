package com.olmos.ingemed;

import com.squareup.otto.Bus;

public class InterfaceDevice {

    private Bus bus;

    public InterfaceDevice(Bus bus){
        this.bus = bus;
    }

    //Lee potencia
    public void readPotencia(int potencia){
        bus.post(new SetPotenciaEvent(potencia));
    }

    //Lee Corriente
    public void readCorriente(int Corriente){
        bus.post(new SetCorrienteEvent(Corriente));
    }

    //Lee impedancia
    public void readImpedancia(int Impedancia){
        bus.post(new SetImpedanciaEvent(Impedancia));
    }





    public class SetPotenciaEvent {
        private int potencia;

        public SetPotenciaEvent(int potencia) {
            this.potencia = potencia;
        }

        public int getPotencia() {
            return potencia;
        }
    }

    public class SetCorrienteEvent {
        private int Corriente;

        public SetCorrienteEvent(int Corriente) {
            this.Corriente = Corriente;
        }

        public int getCorriente() {
            return Corriente;
        }
    }

    public class SetImpedanciaEvent {
        private int Impedancia;

        public SetImpedanciaEvent(int Impedancia) {
            this.Impedancia = Impedancia;
        }

        public int getImpedancia() {
            return Impedancia;
        }
    }
}
