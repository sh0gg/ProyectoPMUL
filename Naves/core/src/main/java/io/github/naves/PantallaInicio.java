package io.github.naves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

public class PantallaInicio extends Pantalla {
    @Override
    public boolean teclaAbajo(int keycode) {
        if(keycode== Input.Keys.S)
            Gdx.app.exit();
        else
            juego.irAPantallaJuego();
        return true;
    }

    @Override
    public boolean teclaArriba(int keycode) {
        return false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        juego.sb.begin();
        juego.fuente.draw(juego.sb,"S para salir, otra tecla para jugar", 10,Mundo.ALTO/2);
        juego.sb.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
