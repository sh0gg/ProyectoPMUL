package com.example.examendbr;

final class DbContract {
    private DbContract(){}

    public static final String DB_NAME = "examen.db";
    public static final int DB_VERSION = 1;

    // canales
    public static final String T_CANALES = "canales";
    public static final String CAN_ID = "id";
    public static final String CAN_NOMBRE = "nombre";
    public static final String CAN_DISP = "disponibilidad";
    public static final String CAN_IMAGEN = "imagen";
    public static final String CAN_CUOTA = "cuota";
    public static final String CAN_PRECIO = "precioPorPantalla";

    // bar
    public static final String T_BAR = "bar";
    public static final String BAR_ID = "id";
    public static final String BAR_NOMBRE = "nombre";
    public static final String BAR_NUM_TELES = "numTeles";

    // barCanales (N:M)
    public static final String T_BAR_CANALES = "barCanales";
    public static final String BC_ID_BAR = "idBar";
    public static final String BC_ID_CANAL = "idCanal";
    public static final String BC_NUM_SUSC = "numSuscripciones";
}