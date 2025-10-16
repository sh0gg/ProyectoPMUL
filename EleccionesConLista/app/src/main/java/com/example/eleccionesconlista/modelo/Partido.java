package com.example.eleccionesconlista.modelo;

public class Partido {
    private long codPartido;
    private String nombre;
    private String color;
    private String logo;

    public Partido() {
    }

    public Partido(String nombre) {
        this.nombre = nombre;
    }

    // Getters y setters
    public long getCodPartido() {
        return codPartido;
    }

    public void setCodPartido(long codPartido) {
        this.codPartido = codPartido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
