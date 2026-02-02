package io.github.naves;

import com.badlogic.gdx.InputAdapter;

public class ProcesadorDeEntrada extends InputAdapter {
    MainNaves juego;
    public ProcesadorDeEntrada(MainNaves juego) {
        this.juego = juego;
    }
    @Override
    public boolean keyDown(int keycode) {
        return ((Pantalla)juego.getScreen()).teclaAbajo(keycode);
    }
    @Override
    public boolean keyUp(int keycode) {
        return ((Pantalla)juego.getScreen()).teclaArriba(keycode);
    }
}
