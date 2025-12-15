package com.example.examendbr;

import java.util.ArrayList;
import java.util.List;

public class Canal {
    private String nombre;
    private String disponibilidad;
    private String imagen;
    private int cuota;
    private int precioPorPantalla;
    private List<Suscripcion> suscripciones;

    public Canal(String nombre, String disponibilidad, String imagen, int cuota, int precioPorPantalla) {
        this.nombre = nombre;
        this.disponibilidad = disponibilidad;
        this.imagen = imagen;
        this.cuota = cuota;
        this.precioPorPantalla = precioPorPantalla;
        this.suscripciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCuota() {
        return cuota;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }

    public int getPrecioPorPantalla() {
        return precioPorPantalla;
    }

    public void setPrecioPorPantalla(int precioPorPantalla) {
        this.precioPorPantalla = precioPorPantalla;
    }
    public List<Suscripcion> getSuscripciones() {
        return suscripciones;
    }

    public void addSuscripcion(Bar bar, int numTeles) {
        Suscripcion sus = new Suscripcion(bar, this, numTeles);
        this.suscripciones.add(sus);
        if (!bar.hasSuscripcion(sus)) {
            bar.addSuscripcion(sus);
        }
    }

    public void addSuscripcion(Suscripcion sus) {
        this.suscripciones.add(sus);
    }

    public boolean hasSuscripcion(Suscripcion sus) {
        return this.suscripciones.contains(sus);
    }
}
