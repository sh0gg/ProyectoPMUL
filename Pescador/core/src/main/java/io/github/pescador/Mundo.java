package io.github.pescador;

public final class Mundo {
    private Mundo() {}

    // Mundo lógico (no depende de la resolución real)
    public static final float ANCHO = 480f;
    public static final float ALTO  = 320f;

    // “Mar”: zona donde se mueven peces (debajo del pescador)
    public static final float ALTO_MAR = 200f;

    // Posiciones base
    public static final float PESCADOR_Y = 230f;     // ajusta si lo ves alto/bajo
    public static final float ANZUELO_Y  = 240f;     // punto “arriba” del anzuelo

    // Velocidades
    public static final float PESCADOR_SPEED_X = 200f;
    public static final float ANZUELO_SPEED_Y  = 320f;

    // Juego (tiempo/bonus)
    public static final float TIEMPO_INICIAL = 30f; // segundos
    public static final float BONUS_POR_PEZ  = 3f;  // segundos extra por captura

    // Spawn peces
    public static final float SPAWN_MIN = 0.8f;
    public static final float SPAWN_MAX = 2.2f;

    // Debug (hitboxes)
    public static final boolean DEBUG_HITBOX = true;
}
