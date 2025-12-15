package com.example.examendbr;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AsistenteBD extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "examen.db";
    private static final int VERSION_BD = 1;

    private static List<Canal> listaCanales = new ArrayList<>();

    public AsistenteBD(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);

        // Rellenamos la lista de canales en memoria (solo una vez)
        if (listaCanales.isEmpty()) {
            Canal laUno = new Canal("La 1", "PUBLICO", "uno", 0, 0);
            listaCanales.add(laUno);
            Canal laDos = new Canal("La 2", "PUBLICO", "dos", 0, 0);
            listaCanales.add(laDos);
            Canal champions = new Canal("Champions", "PAGO", "champions", 100, 20);
            listaCanales.add(champions);
            Canal liga = new Canal("Liga", "PAGO", "liga", 100, 20);
            listaCanales.add(liga);
            Canal moto = new Canal("MotoGP", "PAGO", "gp", 100, 20);
            listaCanales.add(moto);
            Canal f1 = new Canal("F1", "PAGO", "f1", 100, 20);
            listaCanales.add(f1);
            Canal cazaYPesca = new Canal("Caza y Pesca", "PAGO", "cazaypesca", 100, 20);
            listaCanales.add(cazaYPesca);
        }
    }

    public static int getDrawableIdByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla Canales
        db.execSQL("CREATE TABLE IF NOT EXISTS canales (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "disponibilidad TEXT NOT NULL," +
                "imagen TEXT NOT NULL," +
                "cuota INTEGER," +
                "precioPorPantalla INTEGER" +
                ")");

        // Tabla Bar
        db.execSQL("CREATE TABLE IF NOT EXISTS bar (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "numTeles INTEGER NOT NULL" +
                ")");

        // Tabla BarCanales
        db.execSQL("CREATE TABLE IF NOT EXISTS barCanales (" +
                "idBar INTEGER NOT NULL," +
                "idCanal INTEGER NOT NULL," +
                "numSuscripciones INTEGER NOT NULL," +
                "PRIMARY KEY (idBar, idCanal)," +
                "FOREIGN KEY (idBar) REFERENCES bar(id)," +
                "FOREIGN KEY (idCanal) REFERENCES canales(id)" +
                ")");

        // Insertar canales de ejemplo
        for (Canal canal : listaCanales) {
            db.execSQL("INSERT INTO canales (nombre, disponibilidad, imagen, cuota, precioPorPantalla) " +
                    "VALUES ('" + canal.getNombre() + "', '" + canal.getDisponibilidad() + "', '" +
                    canal.getImagen() + "', " + canal.getCuota() + ", " + canal.getPrecioPorPantalla() + ")");
        }

        // Bar de ejemplo
        db.execSQL("INSERT INTO bar (nombre, numTeles) VALUES ('Bar Pepe', 3)");

        // Suponiendo que los IDs en canales son 1..N en orden de inserción
        // MotoGP, F1 y Caza y Pesca con num. de pantallas 2, 1 y 3
        // Orden en lista: La1(1),La2(2),Champions(3),Liga(4),MotoGP(5),F1(6),Caza(7)
        db.execSQL("INSERT INTO barCanales (idBar, idCanal, numSuscripciones) VALUES (1, 5, 2)");
        db.execSQL("INSERT INTO barCanales (idBar, idCanal, numSuscripciones) VALUES (1, 6, 1)");
        db.execSQL("INSERT INTO barCanales (idBar, idCanal, numSuscripciones) VALUES (1, 7, 3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No actualizamos versión en el examen
    }

    public static List<Canal> getListaCanales() {
        return listaCanales;
    }

    // Por si quisieras usarlo con la BD (no es imprescindible con tu enfoque)
    public List<Canal> getCanalesDisponibles(int idBar) {
        List<Canal> canalesDisponibles = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT nombre, disponibilidad, imagen, cuota, precioPorPantalla " +
                        "FROM canales", null);

        while (c.moveToNext()) {
            canalesDisponibles.add(new Canal(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getInt(3),
                    c.getInt(4)
            ));
        }
        c.close();
        return canalesDisponibles;
    }

    public boolean suscripcionEnUso(int idBar, int idCanal) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM barCanales WHERE idBar = ? AND idCanal = ?",
                new String[]{String.valueOf(idBar), String.valueOf(idCanal)}
        );
        boolean existe = c.getCount() > 0;
        c.close();
        return existe;
    }

    public static String getNombreBar(int idBar) {
        // En el examen nos vale con esto
        return "Bar Pepe";
    }
}
