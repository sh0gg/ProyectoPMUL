package com.example.examendbr;

public class Suscripcion {
    private Bar bar;
    private Canal canal;
    private int numPantallas;

    public Suscripcion(Bar bar, Canal canal, int numPantallas) {
        this.bar = bar;
        this.canal = canal;
        this.numPantallas = numPantallas;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public Canal getCanal() {
        return canal;
    }

    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    public int getNumPantallas() {
        return numPantallas;
    }

    public void setNumPantallas(int numPantallas) {
        this.numPantallas = numPantallas;
    }
}
