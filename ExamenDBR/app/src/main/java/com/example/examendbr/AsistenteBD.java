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

        // Datos de ejemplo

        Canal laUno = new Canal("La 1", "PUBLICO", "launo", 0, 0);
        listaCanales.add(laUno);
        Canal laDos = new Canal("La 2", "PUBLICO", "lados", 0, 0);
        listaCanales.add(laDos);
        Canal champions = new Canal("Champions", "PAGO", "champions", 100, 20);
        listaCanales.add(champions);
        Canal liga = new Canal("Liga", "PAGO", "liga", 100, 20);
        listaCanales.add(liga);
        Canal moto = new Canal("MotoGP", "PAGO", "motogp", 100, 20);
        listaCanales.add(moto);
        Canal f1 = new Canal("F1", "PAGO", "f1", 100, 20);
        listaCanales.add(f1);
        Canal cazaYPesca = new Canal("Caza y Pesca", "PAGO", "cazapesca", 100, 20);
        listaCanales.add(cazaYPesca);

        String sqlInsert = "INSERT INTO canales (nombre, disponibilidad, imagen, cuota, precioPorPantalla) VALUES ";
        for (Canal canal : listaCanales) {
            String str = sqlInsert + "('" + canal.getNombre() + "', '" + canal.getDisponibilidad() + "', '" + canal.getImagen() + "', " + canal.getCuota() + ", " + canal.getPrecioPorPantalla() + "),";
            db.execSQL(str);
        }

        List <Canal> canalesSuscritos = new ArrayList<>();
        canalesSuscritos.add(f1);
        canalesSuscritos.add(moto);
        canalesSuscritos.add(cazaYPesca);
        List <Integer> numSuscripciones = new ArrayList<>();
        numSuscripciones.add(1);
        numSuscripciones.add(2);
        numSuscripciones.add(3);

        Bar bar = new Bar("Bar Pepe", 3, canalesSuscritos, numSuscripciones);
        db.execSQL("INSERT INTO bar (nombre, numTeles) VALUES ('" + bar.getNombre() + "', " + bar.getNumTeles() + ")");

        for (Canal c : canalesSuscritos) {
            db.execSQL("INSERT INTO barCanales (idBar, idCanal, numSuscripciones) VALUES (1, " + listaCanales.indexOf(c) + ", " + numSuscripciones.get(canalesSuscritos.indexOf(c)) + ")");
        }
//        db.execSQL("INSERT INTO barCanales (idBar, idCanal, numSuscripciones) VALUES (1, 5, 2)");
//        db.execSQL("INSERT INTO barCanales (idBar, idCanal, numSuscripciones) VALUES (1, 6, 1)");
//        db.execSQL("INSERT INTO barCanales (idBar, idCanal, numSuscripciones) VALUES (1, 7, 3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No planeamos actualizar la base de datos
    }

    public static List<Canal> getListaCanales() {
        return listaCanales;
    }

    public List<Canal> getCanalesDisponibles(int idBar) {
        List<Canal> canalesDisponibles = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT barcanales.idCanal, canales.nombre, canales.disponibilidad, canales.imagen, canales.cuota, canales.precioPorPantalla FROM canales JOIN barCanales ON canales.id = barCanales.idCanal WHERE disponibilidad = 'PUBLICO' OR numSuscripciones > 0",
                new String[]{ String.valueOf(idBar) }
        );
        while (c.moveToNext()) {
            canalesDisponibles.add(new Canal(
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getInt(4),
                    c.getInt(5)
            ));
        }
        c.close();
        return canalesDisponibles;
    }

    public boolean suscripcionEnUso(int idBar, int idCanal) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM barCanales WHERE idBar = ? AND idCanal = ?",
                new String[]{ String.valueOf(idBar), String.valueOf(idCanal) }
        );
        return c.getCount() > 0;
    }

    public static String getNombreBar(int idBar) {
        // SQLiteDatabase db = getReadableDatabase();
        String str = "Bar Pepe"; // db.execSQL("SELECT nombre FROM bar WHERE id LIKE 'idBar'"); trampas
        return str;
    }

}
