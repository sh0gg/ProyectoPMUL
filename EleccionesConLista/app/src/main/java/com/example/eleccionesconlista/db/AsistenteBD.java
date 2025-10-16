package com.example.eleccionesconlista;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eleccionesconlista.db.Utiles;

public class AsistenteBD extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "elecciones.db";
    private static final int VERSION_BD = 1;

    private void insertarCandidato(SQLiteDatabase db, String nombre, String apellidos, long codPartido, String foto) {
        ContentValues candidato = new ContentValues();
        candidato.put("nombre", nombre);
        candidato.put("apellidos", apellidos);
        candidato.put("codPartido", codPartido);
        candidato.put("nVotos", 0);
        candidato.put("foto", foto);
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
        String sqlCreateCandidatos = "CREATE TABLE candidatos (codCandidato INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, apellidos TEXT, codPartido INTEGER, nVotos INTEGER, foto TEXT)";
        String sqlCreatePartidos = "CREATE TABLE partidos (codPartido INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, color TEXT, logo TEXT)";
        String sqlCreateUsuarios = "CREATE TABLE usuarios (NIF TEXT PRIMARY KEY, usuario TEXT, password TEXT)";

        db.execSQL(sqlCreateCandidatos);
        db.execSQL(sqlCreatePartidos);
        db.execSQL(sqlCreateUsuarios);

        // CREACION DE LOS PARTIDOS

        ContentValues partido = new ContentValues();
        partido.put("nombre", "Partido Populoso");
        partido.put("color", "4287f5");
        partido.put("logo", "logo_pp");
        long idPP = db.insert("partidos", null, partido);
        partido.clear();
        partido.put("nombre", "Partido socialmente obrero");
        partido.put("color", "f54242");
        partido.put("logo", "logo_psoe");
        long idPSOE = db.insert("partidos", null, partido);
        partido.clear();
        partido.put("nombre", "Box");
        partido.put("color", "afff54");
        partido.put("logo", "logo_box");
        long idBOX = db.insert("partidos", null, partido);
        partido.clear();
        partido.put("nombre", "Restar");
        partido.put("color", "ff1971");
        partido.put("logo", "logo_restar");
        long idRestar = db.insert("partidos", null, partido);
        partido.clear();

        // CREACION DE LOS CANDIDATOS

        insertarCandidato(db, "M Punto", "Rajoy", idPP, "rajoy");
        insertarCandidato(db, "Norberto", "Freijo", idPP, "freijo");
        insertarCandidato(db, "Perro", "Chanche", idPSOE, "chanche");
        insertarCandidato(db, "Frantzisko", "Lopez", idPSOE, "lopez");
        insertarCandidato(db, "Tiago", "Básculas", idBOX, "basculas");
        insertarCandidato(db, "Josefa", "Kilometrán", idBOX, "kilometran");
        insertarCandidato(db, "Holanda", "Nochez", idRestar, "nochez");

        // CREACION DE LOS USUARIOS

        insertarUsuario(db, "53612286E", "dbesada", Utiles.generateHash("dbesada"));
        insertarUsuario(db, "54321747W", "cdelatorre", Utiles.generateHash("cdelatorre"));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}