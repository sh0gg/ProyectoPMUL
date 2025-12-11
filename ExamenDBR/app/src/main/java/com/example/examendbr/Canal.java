package com.example.examendbr;

public class Canal {
    private String nombre;
    private String disponibilidad;
    private String imagen;
    private int cuota;
    private int precioPorPantalla;

    public Canal(String nombre, String disponibilidad, String imagen, int cuota, int precioPorPantalla) {
        this.nombre = nombre;
        this.disponibilidad = disponibilidad;
        this.imagen = imagen;
        this.cuota = cuota;
        this.precioPorPantalla = precioPorPantalla;
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
}
