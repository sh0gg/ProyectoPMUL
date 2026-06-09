package io.github.bender;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class PantallaInicio extends Pantalla {

    Texture imagen;

    @Override
    public void render(float delta) {
        juego.batch.begin();
        juego.batch.draw(imagen, 0, 0, Mundo.ANCHO, Mundo.ALTO);
        juego.font.draw(juego.batch, String.valueOf((int) juego.prefs.getFloat("Tiempo")), Mundo.ANCHO / 2f, Mundo.ALTO / 2f);
        juego.batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.J:
                juego.irAPantallaJuego();
                return true;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;
        }
        return false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        imagen = new Texture("Titulo.png");
    }

    @Override
    public void dispose() {
        if (imagen != null) imagen.dispose();
    }
}
