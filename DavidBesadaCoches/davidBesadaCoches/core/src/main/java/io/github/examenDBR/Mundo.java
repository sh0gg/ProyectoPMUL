package io.github.examenDBR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Mundo {

    public static final float ANCHO = 800;
    public static final float ALTO  = 480;
    public static final float TOP_BAR = 40; // 0 si no hay barra superior

    public static final float MITAD_ANCHO = ANCHO / 2f;
    public static final float MITAD_ALTO  = ALTO  / 2f;

    public static final boolean DEBUG = false; // true para ver hitboxes

    // Dificultades
    public static int NUM_CARRILES = 10;  // normal por defecto
    public static int CRUCES_VICTORIA = 5;

    public static void setNormal() { NUM_CARRILES = 10; CRUCES_VICTORIA = 5;}
    public static void setDificil() { NUM_CARRILES = 20; CRUCES_VICTORIA = 10;}

    // Estado global
    public static int puntos = 0;
    public static int vidas = 3;
    public static boolean victoria = false;
    public static float tiempoFinal = 0f;

    public static float getTamCasilla() { return ANCHO / NUM_CARRILES; }

    // Records
    public static void guardarRecord(float tiempo) {
        Preferences prefs = Gdx.app.getPreferences("record.pref");
        float recAnterior = prefs.getFloat("record", Float.MAX_VALUE);
        if (tiempo < recAnterior) { // TODO: cambia la condición si "más es mejor"
            prefs.putFloat("record", tiempo);
            prefs.flush();
        }
    }

    public static float getRecord() {
        return Gdx.app.getPreferences("record.pref").getFloat("record", 0f);
    }

    public static void borrarRecord() {
        Preferences prefs = Gdx.app.getPreferences("record.pref");
        prefs.clear();
        prefs.flush();
    }
}
