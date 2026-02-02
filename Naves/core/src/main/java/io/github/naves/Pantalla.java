package io.github.naves;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;

public abstract class Pantalla extends ScreenAdapter implements InputProcessor {
    protected static MainNaves juego;
    public abstract boolean teclaAbajo(int keycode);
    public abstract boolean teclaArriba(int keycode);
    static public void setJuego(MainNaves juego) {
        Pantalla.juego = juego;
    }
}
