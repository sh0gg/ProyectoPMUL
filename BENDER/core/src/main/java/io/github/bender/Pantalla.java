package io.github.bender;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;

public abstract class Pantalla extends InputAdapter implements Screen {
    protected static Main juego;
    static public void setJuego(Main juego) {
        Pantalla.juego=juego;
    }
    // Métodos sobrescribibles en las pantallas
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void resize(int width, int height) {}
    @Override public void dispose() {}
}
