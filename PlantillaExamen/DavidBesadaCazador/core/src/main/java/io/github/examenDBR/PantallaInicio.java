package io.github.examenDBR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends Pantalla {

    public PantallaInicio(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        font.draw(batch, "CAZADOR DE SOMBRAS", Mundo.MITAD_ANCHO - 80, Mundo.MITAD_ALTO + 60);
        font.draw(batch, "Record: " + String.format("%.2f", Mundo.getRecord()) + "s", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 20);
        font.draw(batch, "ESPACIO para jugar  |  R borrar record", 20, 50);
        font.draw(batch, "Flechas: mover  |  Z: cambiar forma  |  ESPACIO: disparar", 20, 25);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                game.setScreen(new PantallaJuego(game)); break;
            case Input.Keys.R:
                Mundo.borrarRecord(); break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit(); break;
        }
        return true;
    }
}