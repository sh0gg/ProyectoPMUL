package com.example.ejemplocosas;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AsistenteBD extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "clientes.db";
    private static final int VERSION_BD = 1;

    public AsistenteBD(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateProvincias = "CREATE TABLE provincias (codProvincia INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT)";
        String sqlCreateClientes = "CREATE TABLE clientes (codCliente INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, apellidos TEXT,NIF TEXT,codProvincia INTEGER, VIP INTEGER, latitud REAL, longitud REAL)";
        db.execSQL(sqlCreateProvincias);
        db.execSQL(sqlCreateClientes);
        ContentValues cv = new ContentValues();
        cv.put("nombre", "A Coruña");
        db.insert("provincias", null, cv);
        cv.put("nombre", "Lugo");
        db.insert("provincias", null, cv);
        cv.put("nombre", "Ourense");
        db.insert("provincias", null, cv);
        cv.put("nombre", "Pontevedra");
        db.insert("provincias", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void clientesBase(SQLiteDatabase db) {
        String sqlInsertClientes = "INSERT INTO clientes (nombre, apellidos, NIF, codProvincia, VIP, latitud, longitud) VALUES";
    }
}
    /* public int borrarClientes(ArrayList<Integer> codigosClientes) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int borrados = 0;
        try {
            for (int codCliente : codigosClientes) {
                int result = db.delete("clientes", "codCliente = ?", new String[]{String.valueOf(codCliente)});
                if (result > 0) {
                    borrados++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return borrados;
    }*/