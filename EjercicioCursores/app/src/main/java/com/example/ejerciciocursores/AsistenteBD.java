package com.example.ejerciciocursores;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AsistenteBD extends SQLiteOpenHelper {

    public AsistenteBD(Context context) {
        super(context, "escapes.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla ESCAPES (para el juego ERC)
        db.execSQL("CREATE TABLE escapes(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "camino TEXT)");

        // Tabla STATS (veces jugadas por nº de errores)
        db.execSQL("CREATE TABLE stats(" +
                "errores INTEGER PRIMARY KEY," +
                "partidas INTEGER)");

        db.execSQL("INSERT INTO escapes(nombre,camino) VALUES" +
                "('Salir por el ascensor','↑→→↓')," +
                "('Camino del baño','↑←←↓')," +
                "('Pito pito Gorgorito', '↑←←↓')");

        // ---------- NUEVAS TABLAS PARA NEB ----------

        // Datos de libros
        db.execSQL("CREATE TABLE libros(" +
                "codLibro INTEGER PRIMARY KEY," +
                "titulo TEXT," +
                "autor TEXT)");

        // Posiciones de cada libro en la estantería
        db.execSQL("CREATE TABLE posiciones(" +
                "codLibro INTEGER," +
                "posX INTEGER," +   // fila (1..N)
                "posY INTEGER)");   // columna (1..M)

        // Libros de ejemplo
        db.execSQL("INSERT INTO libros(codLibro,titulo,autor) VALUES" +
                "(1,'Libro 1','El menda')," +
                "(2,'Libro 2','Lerenda')," +
                "(3,'Libro 3','Juanito')," +
                "(4,'Libro 4','Jaimito')," +
                "(5,'Libro 5','Pedro Juan Santiago de los Olmos')");

        // Posiciones de ejemplo (tu tabla)
        // codLibro, posX, posY
        db.execSQL("INSERT INTO posiciones(codLibro,posX,posY) VALUES" +
                "(1,2,3)," +
                "(2,1,4)," +
                "(3,3,1)," +
                "(4,5,2)," +
                "(5,1,3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS escapes");
        db.execSQL("DROP TABLE IF EXISTS stats");
        db.execSQL("DROP TABLE IF EXISTS posiciones");
        db.execSQL("DROP TABLE IF EXISTS libros");
        onCreate(db);
    }

    // ---------- MÉTODOS PARA ERC ----------

    public Cursor obtenerEscapes() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT _id,nombre,camino FROM escapes", null);
    }

    public int obtenerPartidasConfig(int errores) {
        int res = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT partidas FROM stats WHERE errores=?",
                new String[]{String.valueOf(errores)});
        if (c.moveToFirst()) res = c.getInt(0);
        c.close();
        db.close();
        return res;
    }

    public void sumarPartida(int errores) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT partidas FROM stats WHERE errores=?",
                new String[]{String.valueOf(errores)});
        if (c.moveToFirst()) {
            int p = c.getInt(0) + 1;
            db.execSQL("UPDATE stats SET partidas=" + p +
                    " WHERE errores=" + errores);
        } else {
            db.execSQL("INSERT INTO stats(errores,partidas) VALUES(" +
                    errores + ",1)");
        }
        c.close();
        db.close();
    }

    // ---------- MÉTODO PARA NEB ----------

    /**
     * Devuelve un cursor con: codLibro, titulo, autor, posX, posY
     */
    public Cursor obtenerLibrosConPosicion() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT l.codLibro, l.titulo, l.autor, p.posX, p.posY " +
                        "FROM libros l JOIN posiciones p " +
                        "ON l.codLibro = p.codLibro",
                null
        );
    }
}
