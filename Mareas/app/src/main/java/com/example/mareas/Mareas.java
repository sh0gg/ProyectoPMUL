package com.example.mareas;

import java.time.LocalDate;
import java.util.List;

public class Mareas {
    LocalDate data;
    int idPorto;
    int idPortoRef;
    float latitude;
    List<Marea> listaMareas;
    float lonxitude;
    String nomePorto;

    public Mareas(LocalDate data, int idPorto, int idPortoRef, float latitude, List<Marea> listaMareas, float lonxitude, String nomePorto) {
        this.data = data;
        this.idPorto = idPorto;
        this.idPortoRef = idPortoRef;
        this.latitude = latitude;
        this.listaMareas = listaMareas;
        this.lonxitude = lonxitude;
        this.nomePorto = nomePorto;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getIdPorto() {
        return idPorto;
    }

    public void setIdPorto(int idPorto) {
        this.idPorto = idPorto;
    }

    public int getIdPortoRef() {
        return idPortoRef;
    }

    public void setIdPortoRef(int idPortoRef) {
        this.idPortoRef = idPortoRef;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public List<Marea> getListaMareas() {
        return listaMareas;
    }

    public void setListaMareas(List<Marea> listaMareas) {
        this.listaMareas = listaMareas;
    }

    public float getLonxitude() {
        return lonxitude;
    }

    public void setLonxitude(float lonxitude) {
        this.lonxitude = lonxitude;
    }

    public String getNomePorto() {
        return nomePorto;
    }

    public void setNomePorto(String nomePorto) {
        this.nomePorto = nomePorto;
    }
}
