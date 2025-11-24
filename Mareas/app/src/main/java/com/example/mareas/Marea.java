package com.example.mareas;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Marea {
    double altura;
    LocalDate data;
    LocalDateTime hora;
    int idPorto;
    int idTipoMarea;
    String tipoMarea;

    public Marea(double altura, LocalDate data, LocalDateTime hora, int idPorto, int idTipoMarea, String tipoMarea) {
        this.altura = altura;
        this.data = data;
        this.hora = hora;
        this.idPorto = idPorto;
        this.idTipoMarea = idTipoMarea;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public int getIdPorto() {
        return idPorto;
    }

    public void setIdPorto(int idPorto) {
        this.idPorto = idPorto;
    }

    public int getIdTipoMarea() {
        return idTipoMarea;
    }

    public void setIdTipoMarea(int idTipoMarea) {
        this.idTipoMarea = idTipoMarea;
    }

    public String getTipoMarea() {
        return tipoMarea;
    }

    public void setTipoMarea(String tipoMarea) {
        this.tipoMarea = tipoMarea;
    }
}
