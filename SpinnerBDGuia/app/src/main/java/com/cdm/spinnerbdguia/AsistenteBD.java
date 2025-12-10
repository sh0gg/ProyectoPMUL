package com.cdm.spinnerbdguia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * AsistenteBD:
 *  - Crea una BD SQLite con dos tablas: marcas y modelos.
 *  - Inserta algunos datos de ejemplo en onCreate().
 *  - Ofrece métodos para obtener la lista de marcas y los modelos de una marca.
 */
public class AsistenteBD extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "coches.db";
    private static final int VERSION_BD = 1;

    public AsistenteBD(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla MARCAS
        db.execSQL("CREATE TABLE marcas (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL)");

        // Tabla MODELOS
        db.execSQL("CREATE TABLE modelos (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "idMarca INTEGER NOT NULL)");

        // Datos de ejemplo
        insertarMarca(db, "Toyota");
        insertarMarca(db, "Ford");
        insertarMarca(db, "Seat");

        // Modelos de Toyota (idMarca = 1)
        insertarModelo(db, "Corolla", 1);
        insertarModelo(db, "Yaris", 1);

        // Modelos de Ford (idMarca = 2)
        insertarModelo(db, "Focus", 2);
        insertarModelo(db, "Fiesta", 2);

        // Modelos de Seat (idMarca = 3)
        insertarModelo(db, "Ibiza", 3);
        insertarModelo(db, "León", 3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para guía, no implementamos migraciones.
    }

    private void insertarMarca(SQLiteDatabase db, String nombre) {
        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        db.insert("marcas", null, cv);
    }

    private void insertarModelo(SQLiteDatabase db, String nombre, int idMarca) {
        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("idMarca", idMarca);
        db.insert("modelos", null, cv);
    }

    // ================== MÉTODOS PÚBLICOS ==================

    public List<Marca> getMarcas() {
        ArrayList<Marca> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT _id, nombre FROM marcas ORDER BY nombre", null);
        while (c.moveToNext()) {
            lista.add(new Marca(
                    c.getInt(0),
                    c.getString(1)
            ));
        }
        c.close();
        return lista;
    }

    public List<Modelo> getModelosPorMarca(int idMarca) {
        ArrayList<Modelo> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT _id, nombre, idMarca FROM modelos WHERE idMarca=? ORDER BY nombre",
                new String[]{ String.valueOf(idMarca) }
        );
        while (c.moveToNext()) {
            lista.add(new Modelo(
                    c.getInt(0),
                    c.getString(1),
                    c.getInt(2)
            ));
        }
        c.close();
        return lista;
    }
}
