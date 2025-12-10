package com.cdm.spinnerbdguia;

// Clase simple para representar un MODELO
public class Modelo {
    public int id;
    public String nombre;
    public int idMarca;

    public Modelo(int id, String nombre, int idMarca) {
        this.id = id;
        this.nombre = nombre;
        this.idMarca = idMarca;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
