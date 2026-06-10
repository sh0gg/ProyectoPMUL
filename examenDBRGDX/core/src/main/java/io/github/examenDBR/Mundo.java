package io.github.examenDBR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Mundo {
    public static final float ANCHO = 640;
    public static final float ALTO  = 480;

    public static final float TOP_BAR = 40;

    public static final float MITAD_ANCHO = ANCHO / 2f;
    public static final float MITAD_ALTO  = ALTO  / 2f;

    public static final boolean DEBUG = false;
    public static final int MAX_ESCAPADOS = 5;
    public static final int TOTAL_GLOBOS = 15;
    public static int puntos;
    public static int escapados;
    public static boolean victoria;
    public static float tiempoFinal;

    public static float velocidadMin = 200f;
    public static float velocidadMax = 600f;
    public static float spawnMin = 0.5f;
    public static float spawnMax = 2f;

    public static void guardarRecord(float tiempo) {
        Preferences prefs = Gdx.app.getPreferences("record.pref");

        int recPuntos = prefs.getInteger("recordPuntos", 0);
        float recTiempo = prefs.getFloat("recordTiempo", Float.MAX_VALUE);

        if (puntos > recPuntos || (puntos == recPuntos && tiempo < recTiempo)) {
            prefs.putInteger("recordPuntos", puntos);
            prefs.putFloat("recordTiempo", tiempo);
            prefs.flush();
        }
    }

    public static void setFacil() {
        velocidadMin = 100f; velocidadMax = 300f;
        spawnMin = 1f; spawnMax = 3f;
    }
    public static void setNormal() {
        velocidadMin = 200f; velocidadMax = 600f;
        spawnMin = 0.5f; spawnMax = 2f;
    }
    public static void setDificil() {
        velocidadMin = 400f; velocidadMax = 900f;
        spawnMin = 0.2f; spawnMax = 0.8f;
    }
}
