package com.cdm.spinnerbdguia;

// Clase simple para representar una MARCA
public class Marca {
    public int id;
    public String nombre;

    public Marca(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
