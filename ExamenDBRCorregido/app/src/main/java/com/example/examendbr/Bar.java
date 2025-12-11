package com.example.examendbr;

import java.util.ArrayList;
import java.util.List;

public class Bar {
    private String nombre;
    private int numTeles;

    private List<Canal> suscritos = new ArrayList<>();
    private List<Integer> suscripciones = new ArrayList<>();
    private int[] enUso;

    public Bar(String nombre, int numTeles, List<Canal> suscritos, List<Integer> suscripciones) {
        this.nombre = nombre;
        this.numTeles = numTeles;
        this.suscritos = suscritos;
        this.suscripciones = suscripciones;
        this.enUso = new int[suscritos.size()];
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumTeles() {
        return numTeles;
    }

    public List<Canal> getSuscritos() {
        return suscritos;
    }

    public List<Integer> getSuscripciones() {
        return suscripciones;
    }

    public int[] getEnUso() {
        return enUso;
    }

    public void usarCanal(Canal canal) {
        if (canal == null) return;
        int pos = suscritos.indexOf(canal);
        if (pos >= 0) {
            enUso[pos]++;
        }
    }

    public void liberarCanal(Canal canal) {
        if (canal == null) return;
        int pos = suscritos.indexOf(canal);
        if (pos >= 0 && enUso[pos] > 0) {
            enUso[pos]--;
        }
    }

    public void addSuscripcion(Canal canal, int numPantallas) {
        suscritos.add(canal);
        suscripciones.add(numPantallas);
        int[] nuevoEnUso = new int[enUso.length + 1];
        System.arraycopy(enUso, 0, nuevoEnUso, 0, enUso.length);
        nuevoEnUso[enUso.length] = 0;
        enUso = nuevoEnUso;
    }

    public void ampliarSuscripcion(Canal canal) {
        int pos = suscritos.indexOf(canal);
        if (pos >= 0) {
            int actual = suscripciones.get(pos);
            suscripciones.set(pos, actual + 1);
        }
    }

    // NUEVO: para el botón RESET
    public void resetEnUso() {
        for (int i = 0; i < enUso.length; i++) {
            enUso[i] = 0;
        }
    }
}
