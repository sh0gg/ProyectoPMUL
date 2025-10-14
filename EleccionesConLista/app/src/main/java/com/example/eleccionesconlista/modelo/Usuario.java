package com.example.eleccionesconlista.modelo;

public class Usuario {
    private String NIF;
    private String usuario;
    private String password;

    public Usuario() {}

    public Usuario(String NIF, String usuario, String password) {
        this.NIF = NIF;
        this.usuario = usuario;
        this.password = password;
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
}
