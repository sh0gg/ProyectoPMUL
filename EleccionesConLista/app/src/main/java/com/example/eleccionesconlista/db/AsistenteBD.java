package com.example.eleccionesconlista;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AsistenteBD extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "elecciones.db";
    private static final int VERSION_BD = 1;

    private void insertarCandidato(SQLiteDatabase db, String nombre, String apellidos, long codPartido) {
        ContentValues candidato = new ContentValues();
        candidato.put("nombre", nombre);
        candidato.put("apellidos", apellidos);
        candidato.put("codPartido", codPartido);
        candidato.put("nVotos", 0);
        db.insert("candidatos", null, candidato);
    }

    private void insertarUsuario(SQLiteDatabase db, String NIF, String usuario, String password) {
        ContentValues user = new ContentValues();
        user.put("NIF", NIF);
        user.put("usuario", usuario);
        user.put("password", password);
        db.insert("usuarios", null, user);
    }

    public AsistenteBD(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateCandidatos = "CREATE TABLE candidatos (codCandidato INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, apellidos TEXT, codPartido INTEGER, nVotos INTEGER)";
        String sqlCreatePartidos = "CREATE TABLE partidos (codPartido INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT)";
        String sqlCreateUsuarios = "CREATE TABLE usuarios (NIF TEXT PRIMARY KEY, usuario TEXT, password TEXT)";

        db.execSQL(sqlCreateCandidatos);
        db.execSQL(sqlCreatePartidos);
        db.execSQL(sqlCreateUsuarios);

        // CREACION DE LOS PARTIDOS

        ContentValues partido = new ContentValues();
        partido.put("nombre", "Partido Populoso");
        long idPP = db.insert("partidos", null, partido);
        partido.clear();
        partido.put("nombre", "Partido socialmente obrero");
        long idPSOE = db.insert("partidos", null, partido);
        partido.clear();
        partido.put("nombre", "Box");
        long idBOX = db.insert("partidos", null, partido);
        partido.clear();
        partido.put("nombre", "Restar");
        long idRestar = db.insert("partidos", null, partido);
        partido.clear();

        // CREACION DE LOS CANDIDATOS

        insertarCandidato(db, "M Punto", "Rajoy", idPP);
        insertarCandidato(db, "Norberto", "Freijo", idPP);
        insertarCandidato(db, "Perro", "Chanche", idPSOE);
        insertarCandidato(db, "Frantzisko", "Lopez", idPSOE);
        insertarCandidato(db, "Tiago", "Básculas", idBOX);
        insertarCandidato(db, "Josefa", "Kilometrán", idBOX);
        insertarCandidato(db, "Holanda", "Nochez", idRestar);

        // CREACION DE LOS USUARIOS

        insertarUsuario(db, "53612286E", "dbesada", "dbesada");
        insertarUsuario(db, "54321747W", "cdelatorre", "cdelatorre");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}