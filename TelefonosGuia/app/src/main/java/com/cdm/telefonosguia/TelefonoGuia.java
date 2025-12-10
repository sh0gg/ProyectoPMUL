package com.cdm.telefonosguia;

public class TelefonoGuia {

    public enum Estado { LIBRE, LLAMADA_INICIADA, LLAMADA_RECIBIDA }

    private int numero;
    private int numeroDestino;
    private Estado estado;

    public TelefonoGuia(int numero) {
        this.numero = numero;
        this.estado = Estado.LIBRE;
    }

    public int getNumero() { return numero; }
    public int getNumeroDestino() { return numeroDestino; }
    public boolean estoyHablando() { return (estado != Estado.LIBRE); }

    public void setEmpiezaLlamada(int numeroDestino, boolean inicioYo) {
        this.numeroDestino = numeroDestino;
        this.estado = (inicioYo ? Estado.LLAMADA_INICIADA : Estado.LLAMADA_RECIBIDA);
    }

    public void setEstoyLibre() {
        this.estado = Estado.LIBRE;
    }

    @Override
    public String toString() {
        String str = numero + "";
        if (estoyHablando())
            str += (estado == Estado.LLAMADA_INICIADA ? " > " : " < ") + numeroDestino;
        return str;
    }
}
