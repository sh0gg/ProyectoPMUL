package io.github.naves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;

public abstract class Pantalla extends InputAdapter implements Screen {
    protected static MainNaves juego;

    public abstract boolean teclaAbajo(int keycode);
    public abstract boolean teclaArriba(int keycode);

    static public void setJuego(MainNaves juego) {
        Pantalla.juego = juego;
    }

    @Override
    public void show() {
        // Cada vez que se muestra una pantalla, ella misma se pone al mando de la entrada
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        return this.teclaAbajo(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return this.teclaArriba(keycode);
    }
}
