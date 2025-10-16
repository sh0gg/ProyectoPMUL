package com.example.eleccionesconlista.modelo;

public class Candidato {
    private long codCandidato;
    private String nombre;
    private String apellidos;
    private long codPartido;
    private int nVotos;

    private String foto;

    public Candidato() {}

    public Candidato(String nombre, String apellidos, long codPartido, int nVotos, String foto) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.codPartido = codPartido;
        this.nVotos = nVotos;
    }

    // Getters y setters
    public long getCodCandidato() {
        return codCandidato;
    }

    public void setCodCandidato(long codCandidato) {
        this.codCandidato = codCandidato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public long getCodPartido() {
        return codPartido;
    }

    public void setCodPartido(long codPartido) {
        this.codPartido = codPartido;
    }

    public int getNVotos() {
        return nVotos;
    }

    public void setNVotos(int nVotos) {
        this.nVotos = nVotos;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
