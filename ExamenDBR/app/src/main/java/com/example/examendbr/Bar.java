package com.example.examendbr;

import java.util.ArrayList;
import java.util.List;

public class Bar {
    private String nombre;
    private int numTeles;

    private List<Canal> suscritos = new ArrayList<>();
    private List<Integer> suscripciones = new ArrayList<>();
    private int[] enUso = {0,0,0};


    public Bar(String nombre, int numTeles, List<Canal> suscritos, List<Integer> suscripciones) {
        this.nombre = nombre;
        this.numTeles = numTeles;
        this.suscritos = suscritos;
        this.suscripciones = suscripciones;
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
        if (!suscritos.contains(canal)) {
            throw new IllegalArgumentException("El bar no está suscrito a este canal");
        } else {
            enUso[suscritos.indexOf(canal)]++;
        }
    }
    public void liberarCanal(Canal canal) {
        if (!suscritos.contains(canal)) {
            throw new IllegalArgumentException("El bar no está suscrito a este canal");
        } else {
            enUso[suscritos.indexOf(canal)]--;
        }
    }
}
