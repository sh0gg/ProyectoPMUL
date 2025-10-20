package com.example.eleccionesconlista.modelo;

public class Usuario {
    private String NIF;
    private String usuario;
    private String password;
    private int haVotado;

    public Usuario() {}

    public Usuario(String NIF, String usuario, String password, int haVotado) {
        this.NIF = NIF;
        this.usuario = usuario;
        this.password = password;
        this.haVotado = haVotado;
    }

    // Getters y setters
    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHaVotado() {
        return haVotado;
    }

    public void setHaVotado(int haVotado) {
        this.haVotado = haVotado;
    }
}
