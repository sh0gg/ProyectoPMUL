package com.example.examendbr;

import java.util.ArrayList;
import java.util.List;

public class Bar {
    private String nombre;
    private int numTeles;
    private List<Canal> canalesDisponibles = new ArrayList<>();
    private List<Suscripcion> suscripciones;
    private int[] enUso = {0,0,0};


    public Bar(String nombre, int numTeles) {
        this.nombre = nombre;
        this.numTeles = numTeles;
        this.suscripciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumTeles() {
        return numTeles;
    }

    public void setNumTeles(int numTeles) {
        this.numTeles = numTeles;
    }

    public List<Canal> getCanalesDisponibles() {
        return canalesDisponibles;
    }

    public void setCanalesDisponibles(List<Canal> canalesDisponibles) {
        this.canalesDisponibles = canalesDisponibles;
    }

    public List<Suscripcion> getSuscripciones() {
        return suscripciones;
    }

    public void setSuscripciones(List<Suscripcion> suscripciones) {
        this.suscripciones = suscripciones;
    }

    public int[] getEnUso() {
        return enUso;
    }

    public void setEnUso(int[] enUso) {
        this.enUso = enUso;
    }

    ////    public List<Canal> getCanalesSuscritos() {
////        return canalesSuscritos;
////    }
////    public List<Integer> getSuscripcionesCanal() {
////        return suscripcionesCanal;
////    }
//
//    public int[] getEnUso() {
//        return enUso;
//    }

    public void usarCanal(Canal canal) {
        if (!canalesSuscritos.contains(canal)) {
            throw new IllegalArgumentException("El bar no está suscrito a este canal");
        } else {
            enUso[canalesSuscritos.indexOf(canal)]++;
        }
    }
    public void liberarCanal(Canal canal) {
        if (!canalesSuscritos.contains(canal)) {
            throw new IllegalArgumentException("El bar no está suscrito a este canal");
        } else {
            enUso[canalesSuscritos.indexOf(canal)]--;
        }
    }

    public void addSuscripcion(Canal canalNuevo, int numTeles){
        Suscripcion sus = new Suscripcion(this, canalNuevo, numTeles);
        this.suscripciones.add(sus);
        if (!canalNuevo.hasSuscripcion(sus)) {
            canalNuevo.addSuscripcion(sus);
        }
    }

    public void addSuscripcion(Suscripcion sus) {
        this.suscripciones.add(sus);
    }

    public boolean hasSuscripcion(Suscripcion sus) {
        return this.suscripciones.contains(sus);
    }
}
