package com.example.examendbr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AsistenteBD extends SQLiteOpenHelper {

    // Si prefieres, puedes borrar tus constantes y usar solo DbContract.*
    private static final List<Canal> listaCanales = new ArrayList<>();

    public AsistenteBD(Context context) {
        super(context, DbContract.DB_NAME, null, DbContract.DB_VERSION);

        // Rellenamos la lista en memoria (solo una vez)
        if (listaCanales.isEmpty()) {
            listaCanales.add(new Canal("La 1", "PUBLICO", "uno", 0, 0));
            listaCanales.add(new Canal("La 2", "PUBLICO", "dos", 0, 0));
            listaCanales.add(new Canal("Champions", "PAGO", "champions", 100, 20));
            listaCanales.add(new Canal("Liga", "PAGO", "liga", 100, 20));
            listaCanales.add(new Canal("MotoGP", "PAGO", "gp", 100, 20));
            listaCanales.add(new Canal("F1", "PAGO", "f1", 100, 20));
            listaCanales.add(new Canal("Caza y Pesca", "PAGO", "cazaypesca", 100, 20));
        }
    }

    public static List<Canal> getListaCanales() {
        return listaCanales;
    }

    public static int getDrawableIdByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CANALES
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbContract.T_CANALES + " (" +
                DbContract.CAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.CAN_NOMBRE + " TEXT NOT NULL," +
                DbContract.CAN_DISP + " TEXT NOT NULL," +
                DbContract.CAN_IMAGEN + " TEXT NOT NULL," +
                DbContract.CAN_CUOTA + " INTEGER," +
                DbContract.CAN_PRECIO + " INTEGER" +
                ")");

        // BAR
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbContract.T_BAR + " (" +
                DbContract.BAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DbContract.BAR_NOMBRE + " TEXT NOT NULL," +
                DbContract.BAR_NUM_TELES + " INTEGER NOT NULL" +
                ")");

        // BARCANALES
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbContract.T_BAR_CANALES + " (" +
                DbContract.BC_ID_BAR + " INTEGER NOT NULL," +
                DbContract.BC_ID_CANAL + " INTEGER NOT NULL," +
                DbContract.BC_NUM_SUSC + " INTEGER NOT NULL," +
                "PRIMARY KEY (" + DbContract.BC_ID_BAR + ", " + DbContract.BC_ID_CANAL + ")," +
                "FOREIGN KEY (" + DbContract.BC_ID_BAR + ") REFERENCES " + DbContract.T_BAR + "(" + DbContract.BAR_ID + ")," +
                "FOREIGN KEY (" + DbContract.BC_ID_CANAL + ") REFERENCES " + DbContract.T_CANALES + "(" + DbContract.CAN_ID + ")" +
                ")");

        // INSERT canales (MEJOR con ContentValues)
        for (Canal canal : listaCanales) {
            ContentValues cv = new ContentValues();
            cv.put(DbContract.CAN_NOMBRE, canal.getNombre());
            cv.put(DbContract.CAN_DISP, canal.getDisponibilidad());
            cv.put(DbContract.CAN_IMAGEN, canal.getImagen());
            cv.put(DbContract.CAN_CUOTA, canal.getCuota());
            cv.put(DbContract.CAN_PRECIO, canal.getPrecioPorPantalla());
            db.insert(DbContract.T_CANALES, null, cv);
        }

        // INSERT bar ejemplo
        ContentValues cvBar = new ContentValues();
        cvBar.put(DbContract.BAR_NOMBRE, "Bar Pepe");
        cvBar.put(DbContract.BAR_NUM_TELES, 3);
        long idBar = db.insert(DbContract.T_BAR, null, cvBar); // normalmente será 1

        // Como tus canales se insertan en orden, suelen quedar 1..N.
        // Si quieres “blindaje”, busca ids por nombre en vez de asumir.
        // Aquí lo dejo igual que tu comentario:
        db.execSQL("INSERT INTO " + DbContract.T_BAR_CANALES +
                " (" + DbContract.BC_ID_BAR + ", " + DbContract.BC_ID_CANAL + ", " + DbContract.BC_NUM_SUSC + ") VALUES (" +
                idBar + ", 5, 2)");
        db.execSQL("INSERT INTO " + DbContract.T_BAR_CANALES +
                " (" + DbContract.BC_ID_BAR + ", " + DbContract.BC_ID_CANAL + ", " + DbContract.BC_NUM_SUSC + ") VALUES (" +
                idBar + ", 6, 1)");
        db.execSQL("INSERT INTO " + DbContract.T_BAR_CANALES +
                " (" + DbContract.BC_ID_BAR + ", " + DbContract.BC_ID_CANAL + ", " + DbContract.BC_NUM_SUSC + ") VALUES (" +
                idBar + ", 7, 3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En examen suele valer vacío o drop+create si cambias versión.
    }

    /* =======================
       CRUD “COPIAR/PEGAR”
       ======================= */

    // ---------- INSERT ----------
    public long insertarCanal(String nombre, String disponibilidad, String imagen, int cuota, int precioPorPantalla) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbContract.CAN_NOMBRE, nombre);
        cv.put(DbContract.CAN_DISP, disponibilidad);
        cv.put(DbContract.CAN_IMAGEN, imagen);
        cv.put(DbContract.CAN_CUOTA, cuota);
        cv.put(DbContract.CAN_PRECIO, precioPorPantalla);
        return db.insert(DbContract.T_CANALES, null, cv);
    }

    public long insertarBar(String nombre, int numTeles) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbContract.BAR_NOMBRE, nombre);
        cv.put(DbContract.BAR_NUM_TELES, numTeles);
        return db.insert(DbContract.T_BAR, null, cv);
    }

    // Suscribir un bar a un canal (N:M). Si existe, lo reemplaza.
    public long upsertSuscripcion(int idBar, int idCanal, int numSuscripciones) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbContract.BC_ID_BAR, idBar);
        cv.put(DbContract.BC_ID_CANAL, idCanal);
        cv.put(DbContract.BC_NUM_SUSC, numSuscripciones);
        return db.insertWithOnConflict(DbContract.T_BAR_CANALES, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // ---------- SELECT ----------
    public List<Canal> getTodosLosCanales() {
        List<Canal> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(
                DbContract.T_CANALES,
                new String[]{DbContract.CAN_NOMBRE, DbContract.CAN_DISP, DbContract.CAN_IMAGEN, DbContract.CAN_CUOTA, DbContract.CAN_PRECIO},
                null, null, null, null,
                DbContract.CAN_NOMBRE + " ASC"
        );

        try {
            while (c.moveToNext()) {
                res.add(new Canal(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getInt(3),
                        c.getInt(4)
                ));
            }
        } finally {
            c.close();
        }
        return res;
    }

    public boolean existeSuscripcion(int idBar, int idCanal) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT 1 FROM " + DbContract.T_BAR_CANALES +
                        " WHERE " + DbContract.BC_ID_BAR + "=? AND " + DbContract.BC_ID_CANAL + "=? LIMIT 1",
                new String[]{String.valueOf(idBar), String.valueOf(idCanal)}
        );
        try {
            return c.moveToFirst();
        } finally {
            c.close();
        }
    }

    // Obtener idCanal por nombre (útil para NO asumir ids 1..N)
    public Integer getIdCanalPorNombre(String nombre) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT " + DbContract.CAN_ID +
                        " FROM " + DbContract.T_CANALES +
                        " WHERE " + DbContract.CAN_NOMBRE + "=? LIMIT 1",
                new String[]{nombre}
        );
        try {
            if (c.moveToFirst()) return c.getInt(0);
            return null;
        } finally {
            c.close();
        }
    }

    public String getNombreBar(int idBar) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT " + DbContract.BAR_NOMBRE +
                        " FROM " + DbContract.T_BAR +
                        " WHERE " + DbContract.BAR_ID + "=? LIMIT 1",
                new String[]{String.valueOf(idBar)}
        );
        try {
            return c.moveToFirst() ? c.getString(0) : null;
        } finally {
            c.close();
        }
    }

    // JOIN: canales suscritos por un bar + numSuscripciones
    public Cursor getCanalesSuscritosJoin(int idBar) {
        SQLiteDatabase db = getReadableDatabase();
        String sql =
                "SELECT c." + DbContract.CAN_ID + ", c." + DbContract.CAN_NOMBRE + ", c." + DbContract.CAN_DISP + ", " +
                        "c." + DbContract.CAN_IMAGEN + ", c." + DbContract.CAN_CUOTA + ", c." + DbContract.CAN_PRECIO + ", " +
                        "bc." + DbContract.BC_NUM_SUSC +
                        " FROM " + DbContract.T_BAR_CANALES + " bc" +
                        " JOIN " + DbContract.T_CANALES + " c ON c." + DbContract.CAN_ID + " = bc." + DbContract.BC_ID_CANAL +
                        " WHERE bc." + DbContract.BC_ID_BAR + " = ?" +
                        " ORDER BY c." + DbContract.CAN_NOMBRE + " ASC";
        return db.rawQuery(sql, new String[]{String.valueOf(idBar)});
    }

    // ---------- UPDATE ----------
    public int actualizarPrecioPantalla(int idCanal, int nuevoPrecioPorPantalla) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbContract.CAN_PRECIO, nuevoPrecioPorPantalla);
        return db.update(
                DbContract.T_CANALES,
                cv,
                DbContract.CAN_ID + "=?",
                new String[]{String.valueOf(idCanal)}
        );
    }

    public void incrementarSuscripciones(int idBar, int idCanal, int inc) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "UPDATE " + DbContract.T_BAR_CANALES +
                        " SET " + DbContract.BC_NUM_SUSC + " = " + DbContract.BC_NUM_SUSC + " + ?" +
                        " WHERE " + DbContract.BC_ID_BAR + "=? AND " + DbContract.BC_ID_CANAL + "=?",
                new Object[]{inc, idBar, idCanal}
        );
    }

    // ---------- DELETE ----------
    public int borrarCanalPorId(int idCanal) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                DbContract.T_CANALES,
                DbContract.CAN_ID + "=?",
                new String[]{String.valueOf(idCanal)}
        );
    }

    public int borrarBarPorId(int idBar) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                DbContract.T_BAR,
                DbContract.BAR_ID + "=?",
                new String[]{String.valueOf(idBar)}
        );
    }

    public int borrarSuscripcion(int idBar, int idCanal) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                DbContract.T_BAR_CANALES,
                DbContract.BC_ID_BAR + "=? AND " + DbContract.BC_ID_CANAL + "=?",
                new String[]{String.valueOf(idBar), String.valueOf(idCanal)}
        );
    }
}
