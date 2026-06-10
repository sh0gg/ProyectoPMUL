package io.github.examenDBR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends Pantalla {

    int dificultad = 0; // 0=normal, 1=difícil
    String[] nombres = {"NORMAL", "DIFICIL"};

    public PantallaInicio(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        font.draw(batch, "FROGGER", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 80);
        font.draw(batch, "Record: " + String.format("%.2f", Mundo.getRecord()) + "s", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 40);

        for (int i = 0; i < nombres.length; i++) {
            if (i == dificultad) font.setColor(Color.YELLOW);
            else font.setColor(Color.GRAY);
            font.draw(batch, nombres[i], 250 + i * 200, Mundo.MITAD_ALTO);
        }
        font.setColor(Color.WHITE);

        font.draw(batch, "< > para cambiar  |  ESPACIO para jugar  |  R borrar record", 20, 25);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                dificultad = Math.max(0, dificultad - 1); break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                dificultad = Math.min(1, dificultad + 1); break;
            case Input.Keys.SPACE:
                if (dificultad == 0) Mundo.setNormal();
                else Mundo.setDificil();
                game.setScreen(new PantallaJuego(game));
                break;
            case Input.Keys.R:
                Mundo.borrarRecord(); break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit(); break;
        }
        return true;
    }
}