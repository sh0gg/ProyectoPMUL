package com.example.examenrecdbr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AsistenteBD extends SQLiteOpenHelper {
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO: crear registros y tablas
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS REGISTRO " +
                "(NUMERO INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ID INTEGER NOT NULL," +
                "REGISTRO TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // vacio
    }

    public AsistenteBD(Context context) {
        super(context,"ordenadores",null,1);
    }

    public void registrar(int id, String texto) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO REGISTRO VALUES (?, ?)";
        db.execSQL(sql,new Object[]{id, texto});
    }

    public void borrarRegistros(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM REGISTRO WHERE ID = ?";
        db.execSQL(sql,new Object[] {id});
    }
}
