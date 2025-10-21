package com.example.eleccionesconlista.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.eleccionesconlista.modelo.Candidato;
import com.example.eleccionesconlista.modelo.Partido;
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



    // estos metodos están mal -> como comentó el profe es una gilipollez que ande a llamar a la bd
    // cada vez que necesito un dato, debería usar un join y luego guardar estos datos por ahi.
    // (para no ralentizar el programa, como son pocos candidatos y partidos no se nota)

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
                candidato.setFoto(cursor.getString(5));
                candidatos.add(candidato);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return candidatos;
    }

    public Partido getPartido(long codPartido) {
        Partido partido = new Partido();
        Cursor cursor = db.rawQuery("SELECT * FROM partidos WHERE codPartido = ?", new String[]{String.valueOf(codPartido)});
        if (cursor.moveToFirst()) {
            partido.setCodPartido(cursor.getLong(0));
            partido.setNombre(cursor.getString(1));
            partido.setColor(cursor.getString(2));
            partido.setLogo(cursor.getString(3));
            cursor.close();
        }
        return partido;
    }

    public Usuario getUsuario(String usuario) {
        Usuario user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", new String[]{usuario});
        if (cursor.moveToFirst()) {
            user = new Usuario();
            user.setNIF(cursor.getString(0));
            user.setUsuario(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setHaVotado(cursor.getInt(3));
            cursor.close();
        }
        return user;
    }

    public Usuario getUsuarioPorNif(String nif) {
        Usuario user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE NIF = ?", new String[]{nif});
        if (cursor.moveToFirst()) {
            user = new Usuario();
            user.setNIF(cursor.getString(0));
            user.setUsuario(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setHaVotado(cursor.getInt(3));
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

// método para votar

    public void votar(long[] codCandidatos, String nif) {
        db.beginTransaction();
        try {
            for (long codCandidato : codCandidatos) {
                db.execSQL("UPDATE candidatos SET nVotos = nVotos + 1 WHERE codCandidato = ?", new Object[]{codCandidato});
            }

            ContentValues values = new ContentValues();
            values.put("haVotado", 1);
            db.update("usuarios", values, "NIF = ?", new String[]{nif});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
