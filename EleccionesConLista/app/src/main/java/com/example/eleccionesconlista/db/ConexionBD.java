package com.example.eleccionesconlista.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.eleccionesconlista.AsistenteBD;
import com.example.eleccionesconlista.modelo.Candidato;
import com.example.eleccionesconlista.modelo.Usuario;

import java.util.ArrayList;

public class ConexionBD {
    private AsistenteBD asistenteBD;
    private SQLiteDatabase db;

    public ConexionBD(Context context) {
        asistenteBD = new AsistenteBD(context);
    }

    public void abrir() {
        db = asistenteBD.getWritableDatabase();
    }

    public void cerrar() {
        db.close();
    }

    public ArrayList<Candidato> getCandidatos() {
        ArrayList<Candidato> candidatos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM candidatos", null);
        if (cursor.moveToFirst()) {
            do {
                Candidato candidato = new Candidato();
                candidato.setCodCandidato(cursor.getLong(0));
                candidato.setNombre(cursor.getString(1));
                candidato.setApellidos(cursor.getString(2));
                candidato.setCodPartido(cursor.getLong(3));
                candidato.setNVotos(cursor.getInt(4));
                candidatos.add(candidato);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return candidatos;
    }

    public Usuario getUsuario(String usuario) {
        Usuario user = new Usuario();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", new String[]{usuario});
        if (cursor.moveToFirst()) {
            user.setNIF(cursor.getString(0));
            user.setUsuario(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            cursor.close();
        }
        return user;
    }

    public boolean login(String NIF, String password) {
        String hashedPswd = Utiles.generateHash(password);
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE NIF = ? AND password = ?", new String[]{NIF, hashedPswd});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    public void votar(long[] codCandidatos) {
        for (long codCandidato : codCandidatos) {
            Cursor cursor = db.rawQuery("SELECT * FROM candidatos WHERE codCandidato = ?", new String[]{String.valueOf(codCandidato)});
            if (cursor.moveToFirst()) {
                int nVotos = cursor.getInt(4) + 1;
                db.execSQL("UPDATE candidatos SET nVotos = ? WHERE codCandidato = ?", new Object[]{nVotos, codCandidato});
            }
            cursor.close();
        }
    }
}
