package com.example.fragmentotelefono;

public class Telefono {
    private int id;
    private boolean ocupado;
    private boolean comunicando;
    private Telefono hablaCon;

    public Telefono(int id) {
        this.id = id;
        this.ocupado = false;
        this.comunicando = false;
        this.hablaCon = null;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public Telefono getHablaCon() {
        return hablaCon;
    }

    public void setHablaCon(Telefono hablaCon) {
        this.hablaCon = hablaCon;
    }

    public boolean isComunicando() {
        return comunicando;
    }

    public void setComunicando(boolean comunicando) {
        this.comunicando = comunicando;
    }

    // Métodos
    public boolean llamar(Telefono otroTlf) throws InterruptedException {

        if (otroTlf == null || otroTlf.isOcupado()) {
            return false; // no se puede llamar si el otro está ocupado
        }

        // ocupamos LOS DOS telefonos

        this.ocupado = true;
        otroTlf.setOcupado(true);
        this.hablaCon = otroTlf;
        otroTlf.setHablaCon(this);

        return true;
    }

    public void terminaLlamada() {
        if (this.hablaCon != null) {
            this.hablaCon.setOcupado(false);
            this.hablaCon.setHablaCon(null);
        }
        this.setOcupado(false);
        this.setHablaCon(null);
    }
}
